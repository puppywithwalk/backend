package com.puppypaws.project.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KaKaoUser extends OAuth2ProviderUser{
    public KaKaoUser(OAuth2User oAuth2User, ClientRegistration clientRegistration){
        super(oAuth2User.getAttributes() , oAuth2User, clientRegistration);
    }
        @Override
        public String getId() {
            return (String) getAttributes().get("sub");
        }
        @Override
        public String getEmail() {
            return (String) ((Map) getAttributes().get("kakao_account")).get("email");
        }
        @Override
        public String getUsername() {
            return (String) ((Map) getAttributes().get("properties")).get("nickname");
        }
    }
