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
public class UserSaveRequest {
    private Long id;

    @NotBlank(message ="[loginName] can not be empty")
    private String loginName;

    @NotBlank(message ="[name] can not be empty")
    private String name;

    @NotBlank(message ="[password] can not be empty")
//    @Length(min=6, max=20, message = "[password] 6~20 words")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【password】至少包含 数字和英文，长度6-32")
    private String password;
}