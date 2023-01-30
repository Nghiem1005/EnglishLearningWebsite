package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.entities.RefreshToken;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public interface RefreshTokenService {
  RefreshToken createRefreshToken(Authentication authentication);
  RefreshToken verifyExpiration(RefreshToken token);
  Optional<RefreshToken> findByToken(String token);
}
