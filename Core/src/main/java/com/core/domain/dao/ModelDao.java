package com.core.domain.dao;

import com.core.domain.Model;
import com.core.domain.Model.PrimaryKey;

import com.core.domain.dto.ModelReqDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ModelDao extends MongoRepository<Model, String> {
    List<Model> findAll();
    Model findOneBy_id(PrimaryKey _id);
    Model save(Model m);
    Model insert(Model m);
    void deleteBy_id(PrimaryKey _id);

    default Model findOneBy_id(String model, String project) {
        PrimaryKey _id = new PrimaryKey(model, project);
        System.out.println(model + " " + project);
        return findOneBy_id(_id);
    }
    default void deleteBy_id(String model, String project){
        PrimaryKey id = new PrimaryKey(model, project);
        deleteBy_id(id);
    }
}
