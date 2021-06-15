package com.andrew.wiki.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EBookQueryRequest extends PageRequest{

//    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    private Long category2Id;

    private String name;

    private String description;

}
