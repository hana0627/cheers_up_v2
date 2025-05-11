package com.hana.cheers_up.global.security.jwt.config;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    //TODO 복붙해서 가져오긴 했는데, 이거 내껄로
    public UserAccountDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserAccountDto
                .builder()
                .email((String)attributes.get("email"))
                .nickname((String)attributes.get("name"))
                .build();
    }
}
