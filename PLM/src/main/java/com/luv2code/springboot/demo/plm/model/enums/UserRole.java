package com.luv2code.springboot.demo.plm.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("Người dùng"),
    ADMIN("Quản trị viên");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

}
