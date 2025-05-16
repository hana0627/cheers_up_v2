package com.hana.cheers_up.global.config.oauth2.provider.impl;

import com.hana.cheers_up.global.config.oauth2.provider.Oauth2UserInfo;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode
public class KakaoUserInfo implements Oauth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (!kakaoAccount.containsKey("has_email") || !kakaoAccount.containsKey("email_needs_agreement")) {
            return "email";
        }

        boolean hasEmail = (Boolean) kakaoAccount.get("has_email");
        boolean emailNeedsAgreement = (Boolean) kakaoAccount.get("email_needs_agreement");

        if (hasEmail && emailNeedsAgreement) {
            return (String) kakaoAccount.get("email");
        }

        return "email";
    }

    // TODO name 만들기
    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }

    @Override
    public String getNickname() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }


    // TODO use ENUM
    @Override
    public String getGender() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (!kakaoAccount.containsKey("has_gender") || !kakaoAccount.containsKey("gender_needs_agreement")) {
            return "unknown";
        }

        boolean hasGender = (Boolean) kakaoAccount.get("has_gender");
        boolean genderNeedsAgreement = (Boolean) kakaoAccount.get("gender_needs_agreement");

        if (hasGender && genderNeedsAgreement) {
            return String.valueOf(kakaoAccount.get("gender"));
        }

        return "unknown";
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (!kakaoAccount.containsKey("has_phone_number") || !kakaoAccount.containsKey("phone_number_needs_agreement")) {
            return null;
        }

        boolean hasPhoneNumber = (Boolean) kakaoAccount.get("has_phone_number");
        boolean phoneNumberNeedsAgreement = (Boolean) kakaoAccount.get("phone_number_needs_agreement");

        if (hasPhoneNumber && phoneNumberNeedsAgreement) {
            return kakaoAccount.get("phone_number").toString();
        }

        return null;
    }
}
