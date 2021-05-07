package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.User;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dto.ProjectReqCreateDto;
import com.auth.domain.dto.ProjectReqDto;
import com.auth.domain.dto.ProjectResDto;
import com.auth.domain.dto.TeamReqCreateDto;
import com.auth.service.ProjectService;
import com.auth.service.TokenHandlerService;
import com.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectCtrl {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenHandlerService tokenHandlerService;

    @GetMapping("/all")
    public List<ProjectResDto> getElements(@RequestHeader String authorization) {
        String username = tokenHandlerService.getUsernameByToken(authorization);
        List<ProjectResDto> projects = projectService.findAllProjects();
        if(projects == null)
            projects = new ArrayList<>();
        for(ProjectResDto dto : projects){
            if(!dto.getCreator().equals(username))
                projects.remove(dto);
        }
		// for each project
		// for each team in project
		// if team contains nikola
        System.out.println(projects);
        return projects;
    }

    @GetMapping("/{id}")
    public ProjectResDto getElement(@PathVariable("id") String id) {
        return projectService.findByName(id);
    }

    @DeleteMapping("")
    public void delete(@ModelAttribute ProjectReqCreateDto dto){
        System.out.println(dto);
        projectService.deleteProjectByName(dto.getName());
    }

    @PostMapping("")
    public ProjectResDto createElement(@ModelAttribute ProjectReqCreateDto dto, @RequestHeader String authorization){
        String username = tokenHandlerService.getUsernameByToken(authorization);
        User creator = new User(userService.findByUsername(username));
        System.out.println(dto.getName());
        return projectService.createNewProject(dto.getName(), creator);
    }

    @PutMapping("")
    public ProjectResDto updateElement(@ModelAttribute ProjectReqDto dto){
        System.out.println(dto);
        return projectService.saveExistingProject(dto);
    }
}
