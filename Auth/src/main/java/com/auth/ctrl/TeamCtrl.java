package com.auth.ctrl;

import com.auth.domain.Team;
import com.auth.domain.dao.TeamDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        teamDao.deleteByName(id);
    }
}
