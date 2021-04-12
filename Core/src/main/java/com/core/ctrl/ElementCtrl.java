package com.core.ctrl;

import com.core.domain.Element;
import com.core.domain.Model;
import com.core.domain.dao.ElementDao;
import com.core.domain.dao.ModelDao;
import com.core.domain.dto.ElementReqDto;
import com.core.domain.dto.ModelReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/elements")
@RequiredArgsConstructor
public class ElementCtrl {
    @Autowired
    private ElementDao elementDao;

    @GetMapping("/all/{id}")
    public List<Element> getAllElements(@PathVariable("id") String id) {
        List<Element> elements = elementDao.findAll();
        for(Element e : elements){
            if(!e.getModel().getName().equals(id))
                elements.remove(e);
        }
        return elements;
    }

    @GetMapping("/{id}")
    public Element getElement(@PathVariable("id") String id) {
        return elementDao.findAllBy_id(id);
    }

    @PostMapping("/")
    public Element postModel(@ModelAttribute ElementReqDto dto){
        Element e = new Element(dto);
        elementDao.insert(e);
        return e;
    }

    @PutMapping("/{id}")
    public Element updateElement(@PathVariable("id") String id, @ModelAttribute Object details){
        Element e = elementDao.findAllBy_id(id);
        if(e == null)
            return elementDao.findAllBy_id(id);
        e.setDetails(details);
        elementDao.save(e);
        return e;
    }

    @DeleteMapping("/{id}")
    public void deleteElement(@PathVariable("id") String id){
        elementDao.deleteById(id);
    }
}
