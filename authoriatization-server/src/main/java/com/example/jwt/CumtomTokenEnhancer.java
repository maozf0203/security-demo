package com.example.jwt;

import com.example.security.CumtomUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;


public class CumtomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CumtomUser cumtomUser = (CumtomUser) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        final Map<String, Object> retMap = new HashMap<>();
        additionalInfo.put("email",cumtomUser.getEmail());
        additionalInfo.put("phone",cumtomUser.getPhone());
        additionalInfo.put("userId",cumtomUser.getUserId());
        additionalInfo.put("nickName",cumtomUser.getNickName());
        retMap.put("additionalInfo",additionalInfo);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(retMap);

        return accessToken;
    }
}
