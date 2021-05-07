package com.core.ctrl;

import com.core.domain.Element;
import com.core.domain.Model;
import com.core.domain.dao.ElementDao;
import com.core.domain.dao.ModelDao;
import com.core.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/models")
@RequiredArgsConstructor
public class ModelCtrl {

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private ElementDao elementDao;

    @GetMapping("/all")
    public List<Model> getModel(@RequestParam String project){
        String proj = project.substring(0, project.indexOf(','));
        System.out.println(proj);
        List<Model> models = modelDao.findAll();
        List<Model> modelsRet = new ArrayList<>();

        for(Model m : models){
            if(m.get_id().getProject() == null) continue;
            if(m.get_id().getProject().equals(proj))
                modelsRet.add(m);
        }
        return modelsRet;
    }
    @GetMapping("")
    public Model getModel(@RequestParam String project, @RequestParam String model){
        System.out.println(project + " " + model);
        String name = model;
        Model m = modelDao.findOneBy_id(name.substring(0, name.indexOf(',')), project.substring(0, project.indexOf(',')));
        //System.out.println(m);
        /*List<Object> details = new ArrayList<>();
        if(m.getDetails() != null)
            details = ((List<Object>) m.getDetails());*/
        System.out.println(m);
        return m;
    }

    @PostMapping("")
    public Model postModel(@ModelAttribute ModelReqDto dto){
        Model m = new Model(dto);
        modelDao.insert(m);
        return m;
    }

    @PutMapping("")
    public Model updateModel(@ModelAttribute ModelPutReqDto dto){
        Model m = modelDao.findOneBy_id(dto.getModel(), dto.getProject());
        System.out.println(dto + "\n" + m);
        System.out.println(dto.getDetails());
        if(m == null)
            return modelDao.findOneBy_id(dto.getModel(), dto.getProject());
        m.setDetails(dto.getDetails());
        modelDao.save(m);
        return m;
    }

    @DeleteMapping("")
    public void deleteModel(@ModelAttribute ModelGetReqDto dto){
        modelDao.deleteBy_id(dto.getName(), dto.getProject());
    }
}
