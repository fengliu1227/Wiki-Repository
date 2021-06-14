package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveRequest {
    private Long id;

    private Long parent;

    @NotBlank(message ="[name] can not be empty")
    private String name;

    @NotNull(message ="[sort] can not be empty")
    private Integer sort;
}