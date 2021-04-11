package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.Team;
import com.auth.domain.dao.ProjectDao;
import com.auth.domain.dao.TeamDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/teams")
@RequiredArgsConstructor
public class TeamCtrl {
    @Autowired
    private TeamDao teamDao;

    @GetMapping("/{id}")
    public Team getElement(@PathVariable("id") String id) {
        //System.out.println(teamDao.findByName(id));
        return teamDao.findByName(id);
    }
}
