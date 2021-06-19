package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSetRoleRequest {

    @NotNull(message="[userId] can not be empty")
    private Long id;

    @NotBlank(message ="[role] can not be empty")
    private String role;
}
