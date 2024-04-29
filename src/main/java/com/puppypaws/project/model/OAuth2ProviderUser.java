package com.puppypaws.project.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class OAuth2ProviderUser implements ProviderUser{
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;
    private Map<String, Object> getAttributes;
    public OAuth2ProviderUser(Map<String, Object> getAttributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.getAttributes = getAttributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public List<? extends GrantedAuthority> getAutorities() {
        return oAuth2User.getAuthorities().stream()
                .map(autoritiy -> new SimpleGrantedAuthority(autoritiy.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes;
    }
}
