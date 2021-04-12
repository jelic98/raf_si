package com.auth.service.impl;

import com.auth.domain.Project;
import com.auth.domain.Team;
import com.auth.domain.User;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dao.TeamDao;
import com.auth.domain.dto.ProjectResDto;
import com.auth.domain.dto.TeamResDto;
import com.auth.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;


    @Override
    public ProjectResDto saveExistingProject(ProjectResDto projectResDto) {
        return null;
    }

    @Override
    public ProjectResDto createNewProject(String name, User creator) {
        return saveAndReturnDTO(new Project(name, creator));
    }

    @Override
    public ProjectResDto findByName(String name) {
        return new ProjectResDto(projectDao.findByName(name));
    }

    @Override
    public void deleteProjectByName(String name) {
        projectDao.deleteById(name);
    }

    @Override
    public List<ProjectResDto> findAllProjects() {
        List<ProjectResDto> projects = new ArrayList<>();
        for(Project p : projectDao.findAll()){
            projects.add(new ProjectResDto(p));
        }
        return projects;
    }

    private ProjectResDto saveAndReturnDTO(Project project){

        projectDao.save(project);
        ProjectResDto toReturn = new ProjectResDto(project);
        return toReturn;

    }
}
