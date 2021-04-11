package com.auth.domain.dto;

import com.auth.domain.Role;
import com.auth.domain.Team;
import com.auth.domain.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResDto {

    private String username;
    private String email;
    private String password;
    private List<Team> teams;
    private Role role;
    private List<String> projects;

    public UserResDto(User user){
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
        role = user.getRole();
        /*if(user.getProjects() != null)
            projects = user.getProjects();*/
        if(user.getTeam() != null)
            teams = user.getTeam();
    }
}
