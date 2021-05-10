package com.auth.domain.dto;

import com.auth.domain.Project;
import com.auth.domain.Team;
import com.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamResDto {

    private String name;
    private List<String> users;
    private List<String> projects;
    private String creator;

    public TeamResDto(Team team){
        name = team.getName();
        users = new ArrayList<>();
        if(team.getUser() != null)
            for(User u : team.getUser()){
                users.add(u.getUsername());
            }
        projects = new ArrayList<>();
        if(team.getProject() != null)
            for(Project u : team.getProject()){
                users.add(u.getName());
            };
        creator = team.getCreator().getUsername();
    }
}
