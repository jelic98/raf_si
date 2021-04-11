package com.auth.service;

import com.auth.domain.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
