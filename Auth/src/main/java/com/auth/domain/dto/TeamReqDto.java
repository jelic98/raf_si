package com.auth.domain.dto;

import com.auth.domain.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamReqDto {

    @NotNull
    private String name;

    private User creator;
}
