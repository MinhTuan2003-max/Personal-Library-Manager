package com.luv2code.springboot.demo.plm.service.impl;

import com.luv2code.springboot.demo.plm.model.entity.User;
import com.luv2code.springboot.demo.plm.repository.UserRepository;
import com.luv2code.springboot.demo.plm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(OAuth2AuthenticationToken token) {
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name((String) attributes.get("name"))
                            .picture((String) attributes.get("picture"))
                            .budget(BigDecimal.ZERO)
                            .totalSpent(BigDecimal.ZERO)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return userRepository.save(newUser);
                });
    }

    @Override
    public User addBudget(String userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setBudget(user.getBudget().add(amount));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }
}