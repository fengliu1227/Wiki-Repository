package com.andrew.wiki.controller;

import com.andrew.wiki.request.*;
import com.andrew.wiki.response.UserLoginResponse;
import com.andrew.wiki.response.UserQueryResponse;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.UserService;
import com.andrew.wiki.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlake snowFlake;


    @GetMapping("/user")
    public CommonResponse getAll(){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<UserQueryResponse> list = userService.getAll();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/user/list")
    public CommonResponse getList(@Valid UserQueryRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<UserQueryResponse> list = userService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PutMapping("/user")
    public CommonResponse update(@RequestBody UserSaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        userService.update(req);
        return commonResponse;
    }

    @PostMapping("/user")
    public CommonResponse save(@RequestBody @Valid UserSaveRequest req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResponse commonResponse = new CommonResponse<>();
        userService.save(req);
        return commonResponse;
    }

    @PutMapping("/user/{id}/password")
    public CommonResponse updatePassword(@RequestBody @Valid UserSetPassRequest req,  @PathVariable("id") Long id){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResponse commonResponse = new CommonResponse<>();
        userService.updatePassWord(req, id);
        return commonResponse;
    }

    @DeleteMapping("/user/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        CommonResponse commonResponse = new CommonResponse<>();
        userService.delete(id);
        return commonResponse;
    }

    @PostMapping("/user/login")
    public CommonResponse login(@RequestBody @Valid UserLoginRequest req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResponse<UserLoginResponse> commonResponse = new CommonResponse<>();
        UserLoginResponse userLoginResponse = userService.login(req);
        Long token = snowFlake.nextId();
        userLoginResponse.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), userLoginResponse, 3600* 2, TimeUnit.SECONDS);
        commonResponse.setContent(userLoginResponse);
        return commonResponse;
    }

    @PostMapping("/user/logout/{token}")
    public CommonResponse logout(@PathVariable("token")String token){
        CommonResponse commonResponse = new CommonResponse();
        redisTemplate.delete(token);
        return commonResponse;
    }

    @PutMapping("/user/role")
    public CommonResponse setRole(@RequestBody @Valid UserSetRoleRequest req){
        CommonResponse commonResponse = new CommonResponse();
        System.out.println("============" + req);
        userService.setRole(req);
        return commonResponse;
    }
}
