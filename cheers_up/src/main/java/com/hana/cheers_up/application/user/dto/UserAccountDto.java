package com.hana.cheers_up.application.user.dto;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import lombok.Builder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Builder
public record UserAccountDto(
        String userId,
        String email,
        String nickname,
        String memo,
        RoleType roleType,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static UserAccountDto of(String userId, String email, String nickname, String memo, RoleType roleType) {
        return new UserAccountDto(userId, email, nickname, memo, roleType, LocalDateTime.now(), null,  LocalDateTime.now(), null);
    }

    public static UserAccountDto of(String userId, String email, String nickname, String memo, RoleType roleType, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, email, nickname, memo, roleType, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto from(UserAccount entity) {
        return UserAccountDto.of(
                entity.getUserId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getRoleType(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }


    public UserAccount toEntity() {
        return UserAccount.of(
                userId,
                email,
                nickname,
                memo,
                roleType,
                createdBy
        );
    }
//    @Component
//    public class UserRequestMapper {
//        public UserAccountDto toDto(OAuth2User oAuth2User) {
//            var attributes = oAuth2User.getAttributes();
//            return UserDto.builder()
//                    .email((String)attributes.get("email"))
//                    .name((String)attributes.get("name"))
//                    .picture((String)attributes.get("picture"))
//                    .build();
//        }
//
//        public UserFindRequest toFindDto(UserDto userDto) {
//            return new UserFindRequest(userDto.getEmail());
//        }
//    }

}
