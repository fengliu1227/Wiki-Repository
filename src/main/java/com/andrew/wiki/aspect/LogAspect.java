package com.andrew.wiki.aspect;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.andrew.wiki.util.SnowFlake;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.management.openmbean.TabularDataSupport;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SnowFlake snowFlake;

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /** Define a pointcut */
    @Pointcut("execution(public * com.andrew.wiki.controller..*Controller.*(..))")
    public void controllerPointcut() {}


    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        // Print Request log
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // Print Request Info
        LOG.info("------------- 开始 -------------");
        LOG.info("Request url: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("Controller: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("Remote url: {}", request.getRemoteAddr());


        // Print Request Params
        Object[] args = joinPoint.getArgs();

		Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if ((args[i] instanceof ServletRequest)
                    || (args[i] instanceof ServletResponse)
                    || (args[i] instanceof MultipartFile)) {
                continue;
            }
            arguments[i] = args[i];
        }
        // excludes some fields like password..
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("Request Params: {}", JSONObject.toJSONString(arguments, excludefilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // excludes some fields like password..
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("Result: {}", JSONObject.toJSONString(result, excludefilter));
        LOG.info("------------- End; time consuming：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }
}
