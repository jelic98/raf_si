package com.core.ctrl;

import com.core.domain.Element;
import com.core.domain.Model;
import com.core.domain.dao.ElementDao;
import com.core.domain.dao.ModelDao;
import com.core.domain.dto.ElementDto;
import com.core.domain.dto.ElementReqDto;
import com.core.domain.dto.ModelReqDto;
import com.core.domain.dto.ModelResDto;
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

    @GetMapping("/")
    public List<Model> getModel(){
        System.out.println(modelDao.findAll());
        return modelDao.findAll();
    }
    @GetMapping("/{nameId}/{projectId}")
    public ModelResDto getModel(@PathVariable("nameId") String nameId, @PathVariable("projectId") String projectId){
        Model m = modelDao.findOneBy_id(nameId, projectId);
        List<Object> elems = new ArrayList<>();
        for(Object o : ((List<Object>) m.getElements()))
            elems.add(elementDao.findAllBy_id((String) o));
        return new ModelResDto(m, elems);
    }

    @PostMapping("/")
    public Model postModel(@ModelAttribute ModelReqDto dto){
        Model m = new Model(dto);
        modelDao.insert(m);
        return m;
    }

    @PutMapping("/{nameId}/{projectId}")
    public Model updateModel(@PathVariable("nameId") String nameId, @PathVariable("projectId") String projectId, @ModelAttribute Object elements){
        Model m = modelDao.findOneBy_id(nameId, projectId);
        if(m == null)
            return modelDao.findOneBy_id(nameId, projectId);
        m.setElements(elements);
        modelDao.save(m);
        return m;
    }

    @DeleteMapping("/{nameId}/{projectId}")
    public void deleteModel(@PathVariable("nameId") String nameId, @PathVariable("projectId") String projectId){
        modelDao.deleteBy_id(nameId, projectId);
    }
}
