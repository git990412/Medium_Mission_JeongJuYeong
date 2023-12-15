package com.ll.medium.domain.post.post.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ll.medium.domain.post.post.form.WriteForm;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class ApiV1PostController {
  private final PostService postService;
  private final Rq rq;

  String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";

  @GetMapping("/latest")
  public RsData<?> getLatest30() {
    return RsData.of("200", "success", postService.getLatest30());
  }

  @GetMapping("/list")
  public RsData<?> getList(@RequestParam(value = "page", defaultValue = "0") int page) {
    return RsData.of("200", "success", postService.getList(page));
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/myList")
  public RsData<?> getMyList(@RequestParam(value = "page", defaultValue = "0") int page) {
    long userId = rq.getUserDetails().getId();

    return RsData.of("200", "success", postService.getMyList(page, userId));
  }

  @GetMapping("/{id}")
  public RsData<?> getOne(@PathVariable("id") long id) {
    return RsData.of("200", "success", postService.getOne(id));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/write")
  public RsData<?> writePost(@Valid @RequestBody WriteForm form, BindingResult bindingResult) {
    HashMap<String, String> errors = new HashMap<>();
    if (bindingResult.hasErrors()) {
      bindingResult.getFieldErrors().forEach(e -> {
        errors.put(e.getField(), e.getDefaultMessage());
      });
      return RsData.of("400", "fail", errors);
    }

    postService.writePost(form, rq.getUserDetails().getId());

    return RsData.of("200", "success", null);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}/check")
  public RsData<?> check(@PathVariable("id") long id) {
    Long postMemberId = postService.getPostMemberId(id);
    if (rq.getUserDetails().getId() == postMemberId) {
      return RsData.of("200", "success", null);
    } else {
      return RsData.of("400", "fail", null);
    }
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}/modify")
  public RsData<?> putMethodName(@PathVariable("id") Long id, @Valid @RequestBody WriteForm form,
      BindingResult bindingResult) {
    HashMap<String, String> errors = new HashMap<>();
    if (bindingResult.hasErrors()) {
      bindingResult.getFieldErrors().forEach(e -> {
        errors.put(e.getField(), e.getDefaultMessage());
      });
      return RsData.of("400", "fail", errors);
    }

    Long MemberId = postService.getPostMemberId(id);

    if (rq.getUserDetails().getId() == MemberId) {
      postService.updatePost(form, id);
      return RsData.of("200", "success", null);
    } else {
      return RsData.of("400", "fail", null);
    }
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}/delete")
  public RsData<?> deletePost(@PathVariable("id") Long id) {
    Long MemberId = postService.getPostMemberId(id);

    if (rq.getUserDetails().getId() == MemberId) {
      postService.deletePost(id);
      return RsData.of("200", "success", null);
    } else {
      return RsData.of("400", "fail", null);
    }
  }

  @PutMapping("{id}/hit")
  public RsData<?> increaseHit(@PathVariable("id") Long id) {
    postService.increaseHit(id);
    return RsData.of("200", "success", null);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/imageUpload")
  public RsData<?> uploadImage(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
    if (file.isEmpty()) {
      return RsData.of("400", "fail: no image resource", null);
    }

    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    // 파일을 저장할 경로 설정
    File destination = new File(uploadDir + fileName);

    // 파일 저장
    file.transferTo(destination);

    return RsData.of("200", "success", "/api/v1/post/getImage/" + fileName);
  }

  @GetMapping("/getImage/{fileName}")
  public ResponseEntity<byte[]> getMethodName(@PathVariable("fileName") String fileName) throws IOException {
    Path path = Paths.get(uploadDir + fileName);
    byte[] image = Files.readAllBytes(path);

    return ResponseEntity.ok().body(image);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/comment/write")
  public RsData<?> postMethodName(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
    postService.createComment(id, body.get("body"), rq.getUserDetails().getId());

    return RsData.of("200", "success", null);
  }

}
