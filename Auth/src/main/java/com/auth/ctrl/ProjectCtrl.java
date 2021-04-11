package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.dao.ProjectDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectCtrl {
    @Autowired
    private ProjectDao projectDao;

    @GetMapping("/{id}")
    public Project getElement(@PathVariable("id") String id) {
        return projectDao.findByName(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        projectDao.deleteByName(id);
    }
}
