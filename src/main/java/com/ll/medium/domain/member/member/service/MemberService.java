package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.form.JoinForm;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.domain.member.role.ERole;
import com.ll.medium.domain.member.role.entity.Role;
import com.ll.medium.domain.member.role.repository.RoleRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Transactional
  public void join(JoinForm joinForm) {
    Set<Role> roles = new HashSet<>();
    roles.add(roleRepository.findByName(ERole.ROLE_USER));

    memberRepository.save(
        Member.builder()
            .password(passwordEncoder.encode(joinForm.getPassword()))
            .username(joinForm.getUsername())
            .roles(roles)
            .build());
  }

  public Optional<Member> findByUsername(String username) {
    return memberRepository.findByUsername(username);
  }
}
