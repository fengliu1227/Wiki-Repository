package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocSaveRequest {
    private Long id;

    @NotNull(message ="[ebookId] can not be empty")
    private Long ebookId;

    @NotNull(message ="[parent] can not be empty")
    private Long parent;

    @NotBlank(message ="[name] can not be empty")
    private String name;

    @NotNull(message ="[sort] can not be empty")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    @NotBlank(message ="[content] can not be empty")
    private String content;
}