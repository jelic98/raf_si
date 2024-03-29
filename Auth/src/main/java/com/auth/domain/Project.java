package com.auth.domain;

import com.auth.domain.dto.ProjectReqDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Project")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Project {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @ManyToOne(targetEntity = User.class)
    private User creator;

    @ManyToMany(targetEntity = Team.class)
    private List<Team> team;

    public Project(String name, User creator){
        this.name = name;
        this.creator = creator;
    }
}
