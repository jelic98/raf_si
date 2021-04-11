package com.auth.service.impl;

import com.auth.domain.User;
import com.auth.domain.dao.UserDao;
import com.auth.domain.dto.LoginDto;
import com.auth.domain.dto.UserResDto;
import com.auth.service.AuthService;
import com.auth.service.TokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenHandlerService tokenHandlerService;

    public String login(LoginDto loginDto) {
        User optUser = userDao.findByUsername(loginDto.getUsername());
        if(optUser == null) {
            return null;
        }
        return tokenHandlerService.getTokenByUsername(loginDto.getUsername());
    }
}
