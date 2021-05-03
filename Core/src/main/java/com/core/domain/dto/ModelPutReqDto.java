package com.core.domain.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelPutReqDto {
    private String name;
    private String project;
    private List<Object> elements;
}
