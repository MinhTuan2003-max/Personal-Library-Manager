package com.luv2code.springboot.demo.plm.controller.api;

import com.luv2code.springboot.demo.plm.model.dto.response.ApiResponse;
import com.luv2code.springboot.demo.plm.model.dto.response.UserResponse;
import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(OAuth2AuthenticationToken token) {
        User user = userService.findOrCreateUser(token);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}