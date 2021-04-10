package com.auth.domain.dto;

import com.auth.domain.Project;
import com.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToMany;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamResDto {

    private String name;
    private List<String> users;
    private List<String> projects;
    private List<String> roles;

}
