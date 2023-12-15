package com.ll.medium.global.rq;

import com.ll.medium.global.security.service.UserDetailsImpl;
import com.ll.medium.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final JwtUtils jwtUtils;

  public UserDetailsImpl getUserDetails() {
    return ((UserDetailsImpl)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  public void addCookie(Cookie cookie) {
    response.addCookie(cookie);
  }

  public void deleteCookie(Cookie cookie) {
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  public Cookie[] getCookie() {
    return request.getCookies();
  }

  public String getJwt() {
    return jwtUtils.getJwtFromCookies(request);
  }
}
