package com.aiweb.auth.oauth2;

import com.aiweb.auth.entity.User;
import com.aiweb.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        if ("kakao".equals(registrationId)) {
            return processKakaoUser(oAuth2User);
        }
        
        return oAuth2User;
    }
    
    private OAuth2User processKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        String email = null;
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            email = (String) kakaoAccount.get("email");
        }
        
        if (email == null) {
            // 이메일이 없는 경우 임시 이메일 생성
            Long kakaoId = (Long) attributes.get("id");
            email = "kakao_" + kakaoId + "@kakao.local";
        }
        
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createKakaoUser(email, oAuth2User));
        
        return new KakaoOAuth2User(oAuth2User, email);
    }
    
    private User createKakaoUser(String email, OAuth2User oAuth2User) {
        User user = User.builder()
                .email(email)
                .password(UUID.randomUUID().toString()) // 임시 비밀번호
                .build();
        
        return userRepository.save(user);
    }
}