package com.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelDto {
    private String name;
    private String project;
    private String type;
    private List<ElementDto> elements;
}
