package com.luv2code.springboot.demo.plm.service;

import com.luv2code.springboot.demo.plm.model.entity.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.math.BigDecimal;

public interface UserService {
    User findOrCreateUser(OAuth2AuthenticationToken token);
    User addBudget(String userId, BigDecimal amount);
    User getCurrentUser(String userId);
}