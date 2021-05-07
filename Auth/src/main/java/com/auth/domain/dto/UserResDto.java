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

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResDto {

    private String username;
    private String email;
    private String password;
    private List<String> teams;
    private Role role;

    public UserResDto(User user){
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
        role = user.getRole();
        teams = new ArrayList<>();
        if(user.getTeam() != null) {
            for(Team t : user.getTeam())
                teams.add(t.getName());
        }
    }
}
