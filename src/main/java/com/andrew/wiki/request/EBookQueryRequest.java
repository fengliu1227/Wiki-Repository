package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EBookQueryRequest extends PageRequest{
    private Long id;

    private String name;

    private String description;

}
