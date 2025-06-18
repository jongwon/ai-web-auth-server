package com.aiweb.auth.oauth2;

import com.aiweb.auth.security.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider tokenProvider;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        
        if (email == null) {
            // 카카오는 이메일이 kakao_account 안에 있음
            Object kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount instanceof java.util.Map) {
                email = (String) ((java.util.Map<?, ?>) kakaoAccount).get("email");
            }
        }
        
        String token = tokenProvider.generateTokenFromEmail(email);
        
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("token", token)
                .build()
                .toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}