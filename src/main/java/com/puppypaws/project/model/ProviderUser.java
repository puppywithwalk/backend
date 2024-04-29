package com.puppypaws.project.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public interface ProviderUser {
    String getId();
    String getUsername();
    String getEmail();
    String getProvider();
    List<? extends GrantedAuthority> getAutorities();
    Map<String, Object> getAttributes();
}
