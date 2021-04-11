package com.auth.domain.dto;

import com.auth.domain.Project;
import com.auth.domain.Team;
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
    private List<User> users;
    private List<Project> projects;
    private String creator;

    public TeamResDto(Team team){
        name = team.getName();
        users = team.getUser();
        projects = team.getProject();
        creator = team.getCreator().getUsername();
    }
}
