package com.andrew.wiki.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonResponse<T> {
    private boolean success = true;
    private String message;
    private T content;
}
