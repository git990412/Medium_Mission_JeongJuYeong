package com.ll.medium.jwt.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.jwt.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String refreshToken);

  int deleteByMember(Member member);
}
