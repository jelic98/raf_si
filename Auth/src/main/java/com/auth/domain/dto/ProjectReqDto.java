package com.auth.domain.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectReqDto {

    @NotNull
    private String name;


}
