package com.andrew.wiki.util;


import com.andrew.wiki.response.UserLoginResponse;

import java.io.Serializable;

public class LoginUserContext implements Serializable {

    private static ThreadLocal<UserLoginResponse> user = new ThreadLocal<>();

    public static UserLoginResponse getUser() {
        return user.get();
    }

    public static void setUser(UserLoginResponse user) {
        LoginUserContext.user.set(user);
    }

}
