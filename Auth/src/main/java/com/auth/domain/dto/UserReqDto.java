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
public class UserReqDto {

    @NotNull
    private String username;

    @Length(min = 5, max = 25)
    private String password;
    private String email;

    private String role;

    public UserReqDto(User user){
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
        role = user.getRole().getName();
    }
}
