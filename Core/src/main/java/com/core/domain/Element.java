package com.core.domain;

import com.core.domain.dto.ElementReqDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString

@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "elements")
public class Element {
    @Id
    private String _id;
    private String type;
    private Model.PrimaryKey model;
    private Object details;

    public Element(ElementReqDto dto){
        this.type = dto.getType();
        this.model = new Model.PrimaryKey(dto.getModelName(), dto.getModelProject());
        this.details = dto.getDetails();
    }
}
