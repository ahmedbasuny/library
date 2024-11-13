package com.library.common.security.jwt;

import com.library.domain.user.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserEntity userEntity);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
