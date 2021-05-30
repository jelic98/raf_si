package com.core.domain.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelReqDto {
    private String name;
    private String project;
    private String type;
    private Object details;
}
