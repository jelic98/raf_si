package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.Team;
import com.auth.domain.User;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dao.TeamDao;
import com.auth.domain.dto.TeamReqCreateDto;
import com.auth.domain.dto.TeamResDto;
import com.auth.service.TeamService;
import com.auth.service.TokenHandlerService;
import com.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/teams")
@RequiredArgsConstructor
public class TeamCtrl {
    @Autowired
    private TeamService teamService;

    @Autowired
    private TokenHandlerService tokenHandlerService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public TeamResDto getElement(@PathVariable("id") String id) {
        return teamService.findByName(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        teamService.deleteTeamByName(id);
    }

    @PostMapping("")
    public TeamResDto createElement(@RequestBody TeamReqCreateDto dto, @RequestHeader String authorization){
        String username = tokenHandlerService.getUsernameByToken(authorization);
        User creator = new User(userService.findByUsername(username));
        return teamService.createNewTeam(dto.getName(), creator);
    }

    @GetMapping("/all")
    public List<TeamResDto> getAllElements(){
        return teamService.findAllTeams();
    }
}
