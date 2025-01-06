package com.shuting.flowEdaLearn.commons.config.Oauth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.shuting.flowEdaLearn.entity.user.Oauth2ThirdAccount;
import com.shuting.flowEdaLearn.mapper.user.Oauth2ThirdAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    @Autowired private Oauth2ThirdAccountMapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2ThirdAccount account = null;
        if ("github".equals(registrationId)) {
            account = GithubUserConverter.convert(userRequest, oauth2User);
            QueryWrapper<Oauth2ThirdAccount> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unique_id", account.getUniqueId());
            if (mapper.selectOne(queryWrapper) == null) {
                mapper.insert(account);
            }
        }
        return oauth2User;
    }
}
