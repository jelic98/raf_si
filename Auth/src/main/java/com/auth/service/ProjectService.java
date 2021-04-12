package com.auth.service;

import com.auth.domain.Project;
import com.auth.domain.User;
import com.auth.domain.dto.ProjectReqDto;
import com.auth.domain.dto.ProjectResDto;
import com.auth.domain.dto.TeamReqDto;
import com.auth.domain.dto.TeamResDto;

import java.util.List;

public interface ProjectService {
    ProjectResDto saveExistingProject(ProjectReqDto projectReqDto);
    ProjectResDto createNewProject(String name, User creator);
    ProjectResDto findByName(String name);
    void deleteProjectByName(String name);
    List<ProjectResDto> findAllProjects();
}
