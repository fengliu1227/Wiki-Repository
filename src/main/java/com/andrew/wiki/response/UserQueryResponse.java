package com.andrew.wiki.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryResponse {
    private Long id;

    private String loginName;

    private String name;

    private String role;

    private String password;
}