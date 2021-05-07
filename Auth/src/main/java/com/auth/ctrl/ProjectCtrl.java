package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.User;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dto.*;
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
        List<ProjectResDto> projectsRet = new ArrayList<>();
        UserResDto user = userService.findByUsername(username);
        if(user.getRole().equals("admin")){
            for(ProjectResDto dto : projects){
                if(dto.getCreator() == null) continue;
                if(dto.getCreator().equals(username))
                    projectsRet.add(dto);
            }
        }else{
            List<String> teams = user.getTeams();
            for(ProjectResDto p : projects){
                for(String t : p.getTeams()){
                    for(String myT : teams){
                        if(myT.equals(t))
                            projectsRet.add(p);
                    }
                }
            }
        }


		// for each project
		// for each team in project
		System.out.println(projectsRet);
        return projectsRet;
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
