package com.andrew.wiki.interceptor;

import com.alibaba.fastjson.JSON;
import com.andrew.wiki.response.UserLoginResponse;
import com.andrew.wiki.util.LoginUserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor: unique to the Spring framework, often used for login verification, permission verification, and request log printing
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Print request information
        LOG.info("------------- [LoginInterceptor] start -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // OPTIONS request does not do verification,
        // The front-end and back-end separation architecture, the front-end will send an OPTIONS request to do pre-check first, and do not check the pre-check request
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }

        String path = request.getRequestURL().toString();
        LOG.info("Interface login interception：，path：{}", path);

        //获取header的token参数
        String token = request.getHeader("token");
        LOG.info("Login verification starts，token：{}", token);
        if (token == null || token.isEmpty()) {
            LOG.info( "The token is empty, the request was intercepted" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object object = redisTemplate.opsForValue().get(token);
        if (object == null) {
            LOG.warn( "The token is invaild，the request was intercepted" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            LOG.info("Already login：{}", object);
//            System.out.println(object);
//
//            String string = JSON.toJSON(object).toString();
//            LoginUserContext.setUser(JSON.parseObject((String) object, UserLoginResponse.class));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("------------- [LoginInterceptor] End; Time consuming：{} ms -------------", System.currentTimeMillis() - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        LOG.info("LogInterceptor End");
    }
}
