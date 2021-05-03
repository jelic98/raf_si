package com.core.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelGetReqDto {
    private String name;
    private String project;
}
