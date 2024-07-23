package com.puppypaws.project.service;

import com.puppypaws.project.entity.Member;
import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.common.NotFoundException;
import com.puppypaws.project.model.CustomOAuth2User;
import com.puppypaws.project.model.GoogleUser;
import com.puppypaws.project.model.KaKaoUser;
import com.puppypaws.project.model.ProviderUser;
import com.puppypaws.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        ProviderUser providerUser = providerUser(clientRegistration, oAuth2User);
        String userNameAttributeName = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Long id = register(providerUser);

        return new CustomOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), userNameAttributeName, id);
    }

    private ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        String registrationId = clientRegistration.getRegistrationId();
        if(registrationId.equals("google")){
            return new GoogleUser(oAuth2User, clientRegistration);
        } else if(registrationId.equals("kakao")){
            return new KaKaoUser(oAuth2User, clientRegistration);
        }
        throw new NotFoundException(ErrorCode.ILLEGAL_REGISTRATION_ID);
    }

    private Long register(ProviderUser providerUser) {
        String provider = providerUser.getProvider().equals("google") ? "G" : "K";
        Optional<Member> findUser = memberRepository.findByEmailAndProvider(providerUser.getEmail(), provider);

        if (findUser.isEmpty()) {
            Member member = new Member(
                    providerUser.getUsername(),
                    providerUser.getEmail(),
                    provider
            );

            return memberRepository.save(member).getId();
        } else {
            return findUser.get().getId();
        }
    }
}
