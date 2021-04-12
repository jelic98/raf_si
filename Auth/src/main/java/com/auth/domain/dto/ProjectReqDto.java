package com.auth.domain.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectReqDto {

    @NotNull
    private String name;
    private String teams;
}
