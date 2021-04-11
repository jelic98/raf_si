package com.auth.service.impl;

import com.auth.domain.Team;
import com.auth.domain.User;
import com.auth.domain.dao.TeamDao;
import com.auth.domain.dto.TeamReqDto;
import com.auth.domain.dto.TeamResDto;
import com.auth.domain.dto.UserResDto;
import com.auth.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;

    @Override
    public TeamResDto saveExistingTeam(TeamReqDto teamReqDto) {
        return null;
    }

    @Override
    public TeamResDto createNewTeam(String name, User creator) {
        return saveAndReturnDTO(new Team(name, creator));
    }

    @Override
    public TeamResDto findByName(String name) {
        return new TeamResDto(teamDao.findByName(name));
    }

    @Override
    public void deleteTeamByName(String name) {
        teamDao.deleteById(name);
    }

    @Override
    public List<TeamResDto> findAllTeams() {
        List<Team> allTeams = teamDao.findAll();
        List<TeamResDto> teams = new ArrayList<>();
        for(Team t : allTeams)
            teams.add(new TeamResDto(t));
        return teams;
    }

    private TeamResDto saveAndReturnDTO(Team team){

        teamDao.save(team);
        TeamResDto toReturn = new TeamResDto(team);
        return toReturn;

    }
}
