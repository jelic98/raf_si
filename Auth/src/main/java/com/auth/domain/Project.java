package com.auth.domain;

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
public class Project {

    @Id
    private String name;


    @ManyToMany
    @JoinTable(name = "TeamProject",
            joinColumns = @JoinColumn(name = "ProjectName"),
            inverseJoinColumns = @JoinColumn(name = "teamName"))
    private List<Team> teams;

    //private List<String> models;
}
