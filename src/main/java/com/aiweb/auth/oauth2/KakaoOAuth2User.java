package com.aiweb.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
public class KakaoOAuth2User implements OAuth2User {
    
    private final OAuth2User delegate;
    private final String email;
    
    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }
    
    @Override
    public String getName() {
        return email;
    }
}