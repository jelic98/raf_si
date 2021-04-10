package com.auth.domain.dto;

import com.auth.domain.Team;
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
    private String password;
    private List<String> teams;
}
