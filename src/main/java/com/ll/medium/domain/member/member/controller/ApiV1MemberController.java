package com.ll.medium.domain.member.member.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.form.JoinForm;
import com.ll.medium.domain.member.member.form.LoginForm;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import com.ll.medium.global.security.service.UserDetailsImpl;
import com.ll.medium.jwt.JwtUtils;
import com.ll.medium.jwt.entity.RefreshToken;
import com.ll.medium.jwt.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ApiV1MemberController {
  private final MemberService memberService;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;
  private final Rq rq;

  @PreAuthorize("isAnonymous()")
  @PostMapping("")
  public RsData<?> join(@Valid @RequestBody JoinForm joinForm, BindingResult bindingResult) {
    HashMap<String, String> data = new HashMap<>();

    if (bindingResult.hasErrors()) {

      bindingResult.getFieldErrors().forEach(e -> data.put(e.getField(), e.getDefaultMessage()));
      return RsData.of("400", "Validating Fail", data);

    } else if (!joinForm.getPassword().equals(joinForm.getPasswordConfirm())) {

      data.put("passwordConfirm", "비밀번호가 일치하지 않습니다.");
      return RsData.of("400", "Validating Fail", data);
    }
    try {

      memberService.join(joinForm);

    } catch (DataIntegrityViolationException e) {

      data.put("username", "이미 존재하는 아이디입니다.");
      return RsData.of("400", "Error", data);
    }

    return RsData.of("200", "회원가입 성공", null);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/signout")
  public RsData<?> logoutUser() {
    Long userId = rq.getUserDetails().getId();
    refreshTokenService.deleteByUserId(userId);

    Cookie jwtCookie = jwtUtils.getCleanJwtCookie();
    Cookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

    rq.deleteCookie(jwtCookie);
    rq.deleteCookie(jwtRefreshCookie);

    return RsData.of("200", "로그아웃 성공", null);
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping("/login")
  public RsData<?> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult)
      throws IOException {

    HashMap<String, String> data = new HashMap<>();

    if (bindingResult.hasErrors()) {

      bindingResult.getFieldErrors().forEach(e -> data.put(e.getField(), e.getDefaultMessage()));
      return RsData.of("400", "Validating Fail", data);
    } else if (!memberService.findByUsername(loginForm.getUsername()).isPresent()) {

      data.put("username", "존재하지 않는 아이디입니다.");
      return RsData.of("400", "Validating Fail", data);
    }

    // 인증
    try {

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginForm.getUsername(), loginForm.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 성공 후 반환된 userDetails
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      // jwt 토큰 쿠키 생성
      Cookie jwtCookie = jwtUtils.generateJwtCookie(userDetails.getUsername());

      // refresh 토큰 생성
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);

      // refresh 토큰 쿠키 생성
      Cookie refreshTokenCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

      // 쿠키 전송
      rq.addCookie(jwtCookie);
      rq.addCookie(refreshTokenCookie);

      MemberDto memberDto = new MemberDto();
      memberDto.setUsername(userDetails.getUsername());

      return RsData.of("200", "로그인 성공", memberDto);
    } catch (AuthenticationException e) {

      data.put("password", "비밀번호가 일치하지 않습니다.");
      return RsData.of("400", "Validating Fail", data);
    }
  }

  @PostMapping("/refreshtoken")
  public RsData<?> refreshtoken(HttpServletRequest request) {
    String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

    if ((refreshToken != null) && (refreshToken.length() > 0)) {
      return refreshTokenService
          .findByToken(refreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getMember)
          .map(
              member -> {
                Cookie jwtCookie = jwtUtils.generateJwtCookie(member.getUsername());
                rq.addCookie(jwtCookie);

                return RsData.of("200", "Refresh Token Success", null);
              })
          .orElse(RsData.of("400", "Error token not found", null));
    }

    return RsData.of("400", "Error Empty", null);
  }
}
