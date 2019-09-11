package com.itransition.anton.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by qanto on 02.09.2019.
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
