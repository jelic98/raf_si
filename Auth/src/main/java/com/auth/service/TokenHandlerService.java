package com.auth.service;

public interface TokenHandlerService {

    String getTokenByUsername(String username);

    String getUsernameByToken(String token);
}
