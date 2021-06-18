package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest{

    @NotBlank(message ="[loginName] can not be empty")
    private String loginName;

    @NotBlank(message ="[password] can not be empty")
    @Length(min=6, max=32, message = "[password] 6~32 words")
//    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【password】length 6-32")
    private String password;
}
