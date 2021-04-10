package com.auth.ctrl;

import com.auth.domain.Project;
import com.auth.domain.dao.ProjectDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectCtrl {
    @Autowired
    private ProjectDao projectDao;

    @GetMapping("/{id}")
    public Project getElement(@PathVariable("id") String id) {
        System.out.println(projectDao.findByName(id));
        return projectDao.findByName(id);
    }

}
