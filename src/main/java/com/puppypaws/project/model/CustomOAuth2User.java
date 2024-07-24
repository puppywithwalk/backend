package com.puppypaws.project.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * user의 ID로 사용되는 EMAIL이 겹칠수도 있어 Long타입의 유니크한 ID 사용
 * 현재 어플리케이션에는 ROLE을 사용하지 않아 별도의 ROLE 세팅은 하지 않음
 */
@Getter
public class CustomOAuth2User implements OAuth2User, UserDetails {
    private final Long id;
    private String name;
    private Map<String, Object> attributes;
    private String attributeKey;
    private List<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(Long id, OAuth2ProviderUser oAuth2User, String attributeKey) {
        this.id = id;
        this.name = String.valueOf(id);
        this.attributes = oAuth2User.getAttributes();
        this.attributeKey = attributeKey;
        this.authorities = oAuth2User.getAutorities();
    }

    public CustomOAuth2User(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}