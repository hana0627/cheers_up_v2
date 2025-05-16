package com.hana.cheers_up.global.config.oauth2.provider;

public interface Oauth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getNickname();
    String getGender();
    String getPhoneNumber();
}
