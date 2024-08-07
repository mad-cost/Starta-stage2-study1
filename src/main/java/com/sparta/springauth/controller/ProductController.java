package com.sparta.springauth.controller;

import com.sparta.springauth.dto.ProductRequestDto;
import com.sparta.springauth.entity.User;
import com.sparta.springauth.entity.UserRoleEnum;
import com.sparta.springauth.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
// AuthFilter 가 잘 동작하는지 보기 위해 만듬
public class ProductController {

  @GetMapping("/products")
  public String getProducts(
          // Authentication의 Principle에 들어있는 userDetails 꺼내기.
          @AuthenticationPrincipal UserDetailsImpl userDetails
          ) {


    /* 사용자 정보 꺼내기: UserDetailsServiceImpl에서 loadUserByUsername()로 유저를 조회하고
       조회 성공 유저를 UserDetailsImpl에 전달, 전달된 유저를 바탕으로 getUser()를 사용 하여 유저 정보 꺼내기 */
    User user = userDetails.getUser();
    System.out.println("user.getUsername() = " + user.getUsername());
    System.out.println("user.getEmail() = " + user.getEmail());

    return "redirect:/";
  }

  @Secured(UserRoleEnum.Authority.ADMIN) // 관리자만 접근이 가능하다
  @GetMapping("/products/secured")
  public String getProductsByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
    for (GrantedAuthority authority : userDetails.getAuthorities()) {
      System.out.println("authority.getAuthority() = " + authority.getAuthority());
    }

    return "redirect:/";
  }

  // 유효성 검사
  @PostMapping("/validation")
  @ResponseBody // Postman 사용
  /* 요청 본문 Json형식으로 @RequestBody에 전달된 ProductRequestDto객체의 유효성을 검사하고,
     유효성 검사를 통과한 경우 요청 객체를 반환*/
  public ProductRequestDto testValid(
          @RequestBody
          @Valid
          ProductRequestDto requestDto
  ) {
    return requestDto;
  }
}