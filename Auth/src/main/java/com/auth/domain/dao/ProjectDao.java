package com.auth.domain.dao;

import com.auth.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<Project, String> {
    public Project findByName(String name);
}
