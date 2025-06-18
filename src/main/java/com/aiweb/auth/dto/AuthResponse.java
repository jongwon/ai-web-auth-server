package com.aiweb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private String email;
    
    public AuthResponse(String accessToken, String email) {
        this.accessToken = accessToken;
        this.email = email;
    }
}