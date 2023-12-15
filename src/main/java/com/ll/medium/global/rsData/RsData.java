package com.ll.medium.global.rsData;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = PROTECTED)
@Getter
public class RsData<T> {
  private final String resultCode;
  private final String msg;
  private final T data;
  private final int statusCode;

  public static <T> RsData<T> of(String resultCode, String msg, T data) {
    int statusCode = Integer.parseInt(resultCode);

    return new RsData<>(resultCode, msg, data, statusCode);
  }

  public boolean isSuccess() {
    return statusCode >= 200 && statusCode < 400;
  }

  public boolean isFail() {
    return !isSuccess();
  }

  public RsData<T> of(T data) {
    return RsData.of(resultCode, msg, data);
  }
}
