package com.core.domain.dto;

import com.core.domain.Element;
import com.core.domain.Model;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelResDto {
    private String name;
    private String project;
    private String type;
    private List<Object> details;

    public ModelResDto(Model m, List<Object> details){
        this.name = m.get_id().getName();
        this.project = m.get_id().getProject();
        this.type = m.getType();
        this.details = details;
    }
}
