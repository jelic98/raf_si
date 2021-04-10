package com.auth.domain.dto;

import com.auth.domain.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserReqDto {

    @NotNull
    private String username;

    @Length(min = 5, max = 25)
    private String password;

}
