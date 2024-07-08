package com.pet.Bookshop.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Roles implements GrantedAuthority {
    ADMIN, USER, GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
