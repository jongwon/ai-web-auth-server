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
        Map<String, Object> profile = kakaoAccount != null ? 
                (Map<String, Object>) kakaoAccount.get("profile") : null;
        
        String email = null;
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            email = (String) kakaoAccount.get("email");
        }
        
        if (email == null) {
            // 이메일이 없는 경우 임시 이메일 생성
            Long kakaoId = (Long) attributes.get("id");
            email = "kakao_" + kakaoId + "@kakao.local";
        }
        
        final String finalEmail = email;
        User user = userRepository.findByEmail(finalEmail)
                .map(existingUser -> updateKakaoUser(existingUser, profile))
                .orElseGet(() -> createKakaoUser(finalEmail, profile));
        
        return new KakaoOAuth2User(oAuth2User, finalEmail);
    }
    
    private User createKakaoUser(String email, Map<String, Object> profile) {
        String nickname = profile != null ? (String) profile.get("nickname") : null;
        String profileImage = profile != null ? (String) profile.get("profile_image_url") : null;
        
        User user = User.builder()
                .email(email)
                .password(UUID.randomUUID().toString()) // 임시 비밀번호
                .name(nickname)
                .profileImage(profileImage)
                .build();
        
        return userRepository.save(user);
    }
    
    private User updateKakaoUser(User user, Map<String, Object> profile) {
        if (profile != null) {
            String nickname = (String) profile.get("nickname");
            String profileImage = (String) profile.get("profile_image_url");
            
            if (nickname != null) {
                user.setName(nickname);
            }
            if (profileImage != null) {
                user.setProfileImage(profileImage);
            }
            
            return userRepository.save(user);
        }
        return user;
    }
}