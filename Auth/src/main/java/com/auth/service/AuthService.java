package com.auth.service;

import com.auth.domain.dto.LoginDto;
import com.auth.domain.dto.LoginResDto;

public interface AuthService {
    LoginResDto login(LoginDto loginDto);
}
