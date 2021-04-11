package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.User;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dto.ProjectReqCreateDto;
import com.auth.domain.dto.ProjectResDto;
import com.auth.domain.dto.TeamReqCreateDto;
import com.auth.service.ProjectService;
import com.auth.service.TokenHandlerService;
import com.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ProjectResDto getElement(@PathVariable("id") String id) {
        return projectService.findByName(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        projectService.deleteProjectByName(id);
    }

    @PostMapping("")
    public ProjectResDto createElement(@RequestBody ProjectReqCreateDto dto, @RequestHeader String authorization){
        String username = tokenHandlerService.getUsernameByToken(authorization);
        User creator = new User(userService.findByUsername(username));
        System.out.println(dto.getName());
        return projectService.createNewProject(dto.getName(), creator);
    }
}
