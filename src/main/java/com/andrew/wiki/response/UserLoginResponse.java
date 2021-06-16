package com.andrew.wiki.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse implements Serializable {
    private Long id;

    private String loginName;

    private String name;

    private String role;

    private String token;
}