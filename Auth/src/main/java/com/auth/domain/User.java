package com.auth.domain;

import com.auth.domain.dto.UserReqDto;
import com.auth.domain.dto.UserResDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Table(name = "user")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username")
public class User {

    @Id
    private String username;

    private String password;

    private String email;

    @ManyToMany(targetEntity = Team.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Team> team;

    @ManyToOne(targetEntity = Role.class)
    private Role role;

    public User(UserReqDto dto){
        username = dto.getUsername();
        password = dto.getPassword();
        email = dto.getEmail();
        role = new Role(dto.getRole());
    }

    public User(UserResDto dto){
        username = dto.getUsername();
        password = dto.getPassword();
        email = dto.getEmail();
        role = dto.getRole();
    }
}
