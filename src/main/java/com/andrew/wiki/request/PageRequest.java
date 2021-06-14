package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    @NotNull(message = "[page] can not be empty")
    private int page;

    @NotNull(message="[size] can not be empty")
    @Max(value = 500, message = "[size](the maximum of the items per page) is 500")
    private int size;

}