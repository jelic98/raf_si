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
@Table(name = "Team")
public class Team {
    @Id
    private String name;

    @ManyToMany
    @JoinTable(name = "UserTeam",
            joinColumns = @JoinColumn(name = "teamName"),
            inverseJoinColumns = @JoinColumn(name = "username"))
    private List<User> users;

    @Transient
    @ManyToMany(mappedBy = "teams")
    private List<Project> projects;
}
