package com.ll.medium.jwt.service;

import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.security.service.UserDetailsImpl;
import com.ll.medium.jwt.entity.RefreshToken;
import com.ll.medium.jwt.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final MemberRepository memberRepository;

  @Value("${jwt.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Transactional
  public RefreshToken createRefreshToken(UserDetailsImpl userDetails) {
    RefreshToken refreshToken =
        RefreshToken.builder()
            .member(memberRepository.findById(userDetails.getId()).get())
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
            .build();
    return refreshTokenRepository.save(refreshToken);
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) throws IllegalArgumentException {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByMember(memberRepository.findById(userId).get());
  }
}
