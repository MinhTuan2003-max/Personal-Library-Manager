package com.luv2code.springboot.demo.plm.controller.web;

import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, OAuth2AuthenticationToken token) {
        User user = userService.findOrCreateUser(token);
        model.addAttribute("user", user);
        return "user/profile";
    }
}