package com.auth.service;

import com.auth.domain.User;
import com.auth.domain.dto.UserReqDto;
import com.auth.domain.dto.UserResDto;

public interface UserService {
    UserResDto saveExistingUser(UserReqDto userReqDto);
    UserResDto createNewUser(UserReqDto userReqDto);
    UserResDto findByUsername(String username);
    void deleteUserByUsername(String username);
}
