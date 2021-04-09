package com.core.domain.dao;

import com.core.domain.Element;
import com.core.domain.Model;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ElementDao extends MongoRepository<Element, String> {
    List<Element> findAll();
    Element findAllBy_id(String id);
    Element save(Element e);
    Element insert(Element e);
}
