package com.luv2code.springboot.demo.plm.controller.api;

import com.luv2code.springboot.demo.plm.model.dto.request.BudgetAddRequest;
import com.luv2code.springboot.demo.plm.model.dto.response.ApiResponse;
import com.luv2code.springboot.demo.plm.model.dto.response.UserResponse;
import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(OAuth2AuthenticationToken token) {
        User user = userService.findOrCreateUser(token);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @PostMapping("/budget")
    public ResponseEntity<ApiResponse<UserResponse>> addBudget(
            @Valid @RequestBody BudgetAddRequest request,
            OAuth2AuthenticationToken token) {

        User user = userService.findOrCreateUser(token);
        User updatedUser = userService.addBudget(user.getId(), request.getAmount());
        UserResponse userResponse = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}
