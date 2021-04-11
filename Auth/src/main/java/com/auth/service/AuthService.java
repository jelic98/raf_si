package com.auth.service;

import com.auth.domain.dto.LoginDto;
import com.auth.domain.dto.UserResDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
