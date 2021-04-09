package com.core.domain.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ElementReqDto {
    private String type;
    private String modelName;
    private String modelProject;
    private Object details;
}
