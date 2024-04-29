package com.puppypaws.project.service;

import com.puppypaws.project.entity.Member;
import com.puppypaws.project.model.GoogleUser;
import com.puppypaws.project.model.KaKaoUser;
import com.puppypaws.project.model.ProviderUser;
import com.puppypaws.project.repository.MemberRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        ProviderUser providerUser = providerUser(clientRegistration, oAuth2User);
        register(providerUser);

        return oAuth2User;
    }

    private ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        String registrationId = clientRegistration.getRegistrationId();
        if(registrationId.equals("google")){
            return new GoogleUser(oAuth2User, clientRegistration);
        } else if(registrationId.equals("kakao")){
            return new KaKaoUser(oAuth2User, clientRegistration);
        }
        return null;
    }

    private void register(ProviderUser providerUser){
        Optional<Member> findUser = memberRepository.findByEmail(providerUser.getEmail());
        if(findUser.isEmpty()){
            String provider = providerUser.getProvider().equals("google") ? "G" : "K";
            Member member = new Member();
            member.setNickname(providerUser.getUsername());
            member.setEmail(providerUser.getEmail());
            member.setProvider(provider);
            memberRepository.save(member);
         }
    }
}
