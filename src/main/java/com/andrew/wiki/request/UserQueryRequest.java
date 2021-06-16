package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends PageRequest{

    private String loginName;
}
