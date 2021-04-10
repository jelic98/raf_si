package com.auth.domain.dto;

import com.auth.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectResDto {

    private String name;
    private List<String> teams;
    private List<String> models;

}
