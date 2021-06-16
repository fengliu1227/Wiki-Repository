package com.andrew.wiki.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocVoteRequest {

    @NotNull(message="[userId] can not be empty")
    private Long userId;

    @NotNull(message="[docId] can not be empty")
    private Long docId;
}
