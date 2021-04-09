package com.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ElementDto {
    private String type;
    private String model;
    private List<Object> details;
}
