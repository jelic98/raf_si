package com.auth.domain;

import com.auth.domain.dto.TeamReqDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "Team")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Team {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @NotNull
    @ManyToOne(targetEntity = User.class)
    private User creator;

    @ManyToMany(targetEntity = User.class)
    private List<User> user;

    @ManyToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Project> project;

    public Team(TeamReqDto dto){
        name = dto.getName();
        creator = dto.getCreator();
    }

    public Team(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public Team(String name) {
        this.name = name;
    }
}
