package com.core.domain;

import com.core.domain.dto.ModelReqDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString

@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "models")
public class Model {
    @Id
    private PrimaryKey _id;

    @AllArgsConstructor
    @Value
    public static class PrimaryKey implements Serializable {
        private String name;
        private String project;
    }

    private String type;

    private Object elements;

    public Model(ModelReqDto dto){
        this._id = new PrimaryKey(dto.getName(), dto.getProject());
        this.type = dto.getType();
    }

    public Model(String name, String project, String type, Object elements)  {
        _id = new PrimaryKey(name, project);
        this.type = type;
        this.elements = elements;
    }
}
