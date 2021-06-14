package com.andrew.wiki.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryResponse {
    private Long id;

    private Long parent;

    private String name;

    private Integer sort;
}