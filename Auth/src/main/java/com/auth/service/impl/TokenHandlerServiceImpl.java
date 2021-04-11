package com.auth.service.impl;

import com.auth.service.TokenHandlerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenHandlerServiceImpl implements TokenHandlerService {

    private static final String KEY = "a4B4SjAKwXCP9H37a4B4SjAKwXCP9H37a4B4SjAKwXCP9H37";

    @Override
    public String getTokenByUsername(String username) {
        return Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
    }

    @Override
    public String getUsernameByToken(String token) {
        return Jwts.parser().setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
