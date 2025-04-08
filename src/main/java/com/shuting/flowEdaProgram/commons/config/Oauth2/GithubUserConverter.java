package com.shuting.flowEdaProgram.commons.config.Oauth2;

import com.shuting.flowEdaProgram.entity.user.Oauth2ThirdAccount;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class GithubUserConverter {
    public static Oauth2ThirdAccount convert(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        Oauth2ThirdAccount account = new Oauth2ThirdAccount();
        Map<String, Object> attributes = oauth2User.getAttributes();
        account.setUniqueId((Integer) attributes.get("id"));
        account.setLogin((String) attributes.get("login"));
        account.setName((String) attributes.get("name"));
        account.setAvatarUrl((String) attributes.get("avatar_url"));
        account.setRegistrationId("github");
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        account.setCredentials(accessToken.getTokenValue());
        ZonedDateTime zonedDateTime = accessToken.getExpiresAt().atZone(ZoneId.systemDefault());
        account.setCredentialsExpiresAt(Date.from(zonedDateTime.toInstant()));
        account.setCreateTime(new Date());
        return account;
    }
}
