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
    public List<Model> getModel(/*@PathVariable("id") String id*/){
        System.out.println("TU SAM");
        List<Model> models = modelDao.findAll();
        /*for(Model m : models){
            if(!m.get_id().getProject().equals(id))
                models.remove(m);
        }*/
        return models;
    }
    @GetMapping("")
    public Model getModel(@ModelAttribute ModelGetReqDto dto){
        Model m = modelDao.findOneBy_id(dto.getName(), dto.getProject());
        System.out.println(m);
        List<Object> details = new ArrayList<>();
        if(m.getDetails() != null)
            details = ((List<Object>) m.getDetails());
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
        Model m = modelDao.findOneBy_id(dto.getName(), dto.getProject());
        System.out.println(dto + "\n" + m);
        if(m == null)
            return modelDao.findOneBy_id(dto.getName(), dto.getProject());
        m.setDetails(dto.getDetails());
        modelDao.save(m);
        return m;
    }

    @DeleteMapping("")
    public void deleteModel(@ModelAttribute ModelGetReqDto dto){
        modelDao.deleteBy_id(dto.getName(), dto.getProject());
    }
}
