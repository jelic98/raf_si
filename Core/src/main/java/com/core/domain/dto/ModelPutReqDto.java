package com.core.domain.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelPutReqDto {
    private String model;
    private String project;
    private Object details;
}
