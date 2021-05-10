package com.auth.service;

import com.auth.domain.User;
import com.auth.domain.dto.TeamPutReqDto;
import com.auth.domain.dto.TeamResDto;

import java.util.List;

public interface TeamService {
    TeamResDto saveExistingTeam(TeamPutReqDto team);
    TeamResDto createNewTeam(String name, User creator);
    TeamResDto findByName(String name);
    void deleteTeamByName(String name);
    List<TeamResDto> findAllTeams();
}
