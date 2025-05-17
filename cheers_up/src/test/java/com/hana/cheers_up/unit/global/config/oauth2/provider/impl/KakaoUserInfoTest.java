package com.hana.cheers_up.unit.global.config.oauth2.provider.impl;

import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class KakaoUserInfoTest {
    private Map<String, Object> kakaoAccount;
    private Map<String, Object> attributes;
    private Map<String, Object> properties;

    @BeforeEach
    void setUp() {
        kakaoAccount = new HashMap<>();
        attributes = new HashMap<>();
        properties = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();

        attributes.put("id", 1234567890);
        attributes.put("connected_at", "2023-04-16T11:17:49Z");


        properties.put("nickname", "하나");
        attributes.put("properties", properties);
        kakaoAccount.put("profile_nickname_needs_agreement", false);

        profile.put("nickname", "하나");
        profile.put("is_default_nickname", false);
        kakaoAccount.put("profile", profile);

//        kakaoAccount.put("has_email", true);
//        kakaoAccount.put("email_needs_agreement", false);
        kakaoAccount.put("is_email_valid", true);
        kakaoAccount.put("is_email_verified", true);
//        kakaoAccount.put("email", "shamoo1@naver.com");

        attributes.put("kakao_account", kakaoAccount);
    }

    @Test
    void providerId를_정상적으로_반환한다() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getProviderId();

        //then
        assertThat(result)
                .isEqualTo(String.valueOf(attributes.get("id")));
    }

    @Test
    void getProvider호출시_항상_kakao를_반환한다() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getProvider();

        //then
        assertThat(result).isEqualTo("kakao");
    }

    @Test
    void getNickName호출시_nickname을_반환한다() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getName();

        //then
        assertThat(result).isEqualTo(properties.get("nickname"));
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email과 email_needs_agreement이 없는 경우")
    @Test
    void getEmail호출시_이메일을_반환한다1() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");

    }


    @DisplayName("getEmail호출시_이메일을_반환한다_has_email값만 없는 경우")
    @Test
    void getEmail호출시_이메일을_반환한다2() {
        //given
        kakaoAccount.put("has_email", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");

    }

    @DisplayName("getEmail호출시_이메일을_반환한다_email_needs_agreement값이 없는 경우")
    @Test
    void getEmail호출시_이메일을_반환한다3() {
        //given
        kakaoAccount.put("email_needs_agreement", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");

    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email=true, email_needs_agreement=true")
    @Test
    void getEmail호출시_이메일을_반환한다4() {
        //given
        kakaoAccount.put("has_email", true);
        kakaoAccount.put("email_needs_agreement", true);
        kakaoAccount.put("email", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo(kakaoAccount.get("email"));
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email=true, email_needs_agreement=false")
    @Test
    void getEmail호출시_이메일을_반환한다5() {
        //given
        kakaoAccount.put("has_email", true);
        kakaoAccount.put("email_needs_agreement", false);
        kakaoAccount.put("email", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email=false, email_needs_agreement=true")
    @Test
    void getEmail호출시_이메일을_반환한다6() {
        //given
        kakaoAccount.put("has_email", false);
        kakaoAccount.put("email_needs_agreement", true);
        kakaoAccount.put("email", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email=false, email_needs_agreement=false")
    @Test
    void getEmail호출시_이메일을_반환한다7() {
        //given
        kakaoAccount.put("has_email", false);
        kakaoAccount.put("email_needs_agreement", false);
        kakaoAccount.put("email", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email키가_없고_email_needs_agreement키가_있는_경우")
    @Test
    void getEmail호출시_이메일을_반환한다8() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("email_needs_agreement", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");
    }

    @DisplayName("getEmail호출시_이메일을_반환한다_has_email키는_있고_email_needs_agreement키가_없는_경우")
    @Test
    void getEmail호출시_이메일을_반환한다9() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("has_email", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getEmail();

        //then
        assertThat(result).isEqualTo("email");
    }


    @DisplayName("getGender호출시_성별을_반환한다_has_gender과 gender_needs_agreement이 없는 경우")
    @Test
    void getGender호출시_성별을_반환한다1() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");

    }


    @DisplayName("getGender호출시_성별을_반환한다_has_gender값만 없는 경우")
    @Test
    void getGender호출시_성별을_반환한다2() {
        //given
        kakaoAccount.put("has_gender", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");

    }

    @DisplayName("getGender호출시_성별을_반환한다_gender_needs_agreement값과 없는 경우")
    @Test
    void getGender호출시_성별을_반환한다3() {
        //given
        kakaoAccount.put("gender_needs_agreement", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");

    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender=true, gender_needs_agreement=true")
    @Test
    void getGender호출시_성별을_반환한다4() {
        //given
        kakaoAccount.put("has_gender", true);
        kakaoAccount.put("gender_needs_agreement", true);
        kakaoAccount.put("gender", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo(kakaoAccount.get("gender"));
    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender=true, gender_needs_agreement=false")
    @Test
    void getGender호출시_성별을_반환한다5() {
        //given
        kakaoAccount.put("has_gender", true);
        kakaoAccount.put("gender_needs_agreement", false);
        kakaoAccount.put("gender", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender=false, gender_needs_agreement=true")
    @Test
    void getGender호출시_성별을_반환한다6() {
        //given
        kakaoAccount.put("has_gender", false);
        kakaoAccount.put("gender_needs_agreement", true);
        kakaoAccount.put("gender", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender=false, gender_needs_agreement=false")
    @Test
    void getGender호출시_성별을_반환한다7() {
        //given
        kakaoAccount.put("has_gender", false);
        kakaoAccount.put("gender_needs_agreement", false);
        kakaoAccount.put("gender", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender키가_있고_gender_needs_agreement키가_없는_경우")
    @Test
    void getGender호출시_성별을_반환한다9() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("has_gender", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getGender호출시_성별을_반환한다_has_gender키가_없고_gender_needs_agreement키가_있는_경우")
    @Test
    void getGender호출시_성별을_반환한다8() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("gender_needs_agreement", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getGender();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number과 phone_number_needs_agreement이 없는 경우")
    @Test
    void getPhoneNumber호출시_성별을_반환한다1() {
        //given

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");

    }


    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number값만 없는 경우")
    @Test
    void getPhoneNumber호출시_성별을_반환한다2() {
        //given
        kakaoAccount.put("has_phone_number", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");

    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_phone_number_needs_agreement값과 없는 경우")
    @Test
    void getPhoneNumber호출시_성별을_반환한다3() {
        //given
        kakaoAccount.put("phone_number_needs_agreement", true);
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");

    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number=true, phone_number_needs_agreement=true")
    @Test
    void getPhoneNumber호출시_성별을_반환한다4() {
        //given
        kakaoAccount.put("has_phone_number", true);
        kakaoAccount.put("phone_number_needs_agreement", true);
        kakaoAccount.put("phone_number", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo(kakaoAccount.get("phone_number"));
    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number=true, phone_number_needs_agreement=false")
    @Test
    void getPhoneNumber호출시_성별을_반환한다5() {
        //given
        kakaoAccount.put("has_phone_number", true);
        kakaoAccount.put("phone_number_needs_agreement", false);
        kakaoAccount.put("phone_number", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number=false, phone_number_needs_agreement=true")
    @Test
    void getPhoneNumber호출시_성별을_반환한다6() {
        //given
        kakaoAccount.put("has_phone_number", false);
        kakaoAccount.put("phone_number_needs_agreement", true);
        kakaoAccount.put("phone_number", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getPhoneNumber호출시_성별을_반환한다_has_phone_number=false, phone_number_needs_agreement=false")
    @Test
    void getPhoneNumber호출시_성별을_반환한다7() {
        //given
        kakaoAccount.put("has_phone_number", false);
        kakaoAccount.put("phone_number_needs_agreement", false);
        kakaoAccount.put("phone_number", "hanana1234@naver.com");
        attributes.put("kakao_account", kakaoAccount);

        //when
        String result = new KakaoUserInfo(attributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");
    }
    @DisplayName("getPhoneNumber호출시_전화번호를_반환한다_has_phone_number키가_없고_phone_number_needs_agreement키가_있는_경우")
    @Test
    void getPhoneNumber호출시_전화번호를_반환한다8() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("has_phone_number", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @DisplayName("getPhoneNumber호출시_전화번호를_반환한다_has_phone_number키가_있고_phone_number_needs_agreement키가_없는_경우")
    @Test
    void getPhoneNumber호출시_전화번호를_반환한다9() {
        //given
        Map<String, Object> newKakaoAccount = new HashMap<>();
        newKakaoAccount.put("phone_number_needs_agreement", true);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        newKakaoAccount.put("profile", profile);

        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("kakao_account", newKakaoAccount);

        //when
        String result = new KakaoUserInfo(newAttributes).getPhoneNumber();

        //then
        assertThat(result).isEqualTo("unknown");
    }

    @Test
    void equals_And_hashCode_테스트() {
        //given
        Map<String, Object> attributes1 = new HashMap<>();
        attributes1.put("id", 1234567890);

        Map<String, Object> kakaoAccount1 = new HashMap<>();
        Map<String, Object> profile1 = new HashMap<>();
        profile1.put("nickname", "테스트");
        kakaoAccount1.put("profile", profile1);
        attributes1.put("kakao_account", kakaoAccount1);

        Map<String, Object> attributes2 = new HashMap<>(attributes1);

        KakaoUserInfo userInfo1 = new KakaoUserInfo(attributes1);
        KakaoUserInfo userInfo2 = new KakaoUserInfo(attributes2);
        KakaoUserInfo userInfo3 = new KakaoUserInfo(attributes1);

        // then
        assertThat(userInfo1).isEqualTo(userInfo1); // 자기 자신과 비교
        assertThat(userInfo1).isEqualTo(userInfo2); // 동일한 데이터를 가진 다른 객체와 비교
        assertThat(userInfo1).isEqualTo(userInfo3); // 동일한 데이터를 가진 다른 객체와 비교

        assertThat(userInfo1.hashCode()).isEqualTo(userInfo2.hashCode());
        assertThat(userInfo1.hashCode()).isEqualTo(userInfo3.hashCode());

        // 다른 객체와 비교
        assertThat(userInfo1).isNotEqualTo(new Object());
        assertThat(userInfo1).isNotEqualTo(null);

        // 다른 데이터를 가진 객체와 비교
        Map<String, Object> differentAttributes = new HashMap<>();
        differentAttributes.put("id", 9876543210L);

        Map<String, Object> differentKakaoAccount = new HashMap<>();
        Map<String, Object> differentProfile = new HashMap<>();
        differentProfile.put("nickname", "다른테스트");
        differentKakaoAccount.put("profile", differentProfile);
        differentAttributes.put("kakao_account", differentKakaoAccount);

        KakaoUserInfo differentUserInfo = new KakaoUserInfo(differentAttributes);
        assertThat(userInfo1).isNotEqualTo(differentUserInfo);
    }

}
