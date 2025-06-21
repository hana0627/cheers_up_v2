package com.hana.cheers_up.unit.application.pub.controller.response;

import com.hana.cheers_up.application.pub.controller.response.PubResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PubResponseTest {

    @Test
    void 빌더_toString_메서드_테스트() {
        PubResponse.PubResponseBuilder builder = PubResponse.builder()
                .pubName("쿠시야끼하루 상도점")
                .pubAddress("서울 동작구 상도동 490-5")
                .directionUrl("https://map.kakao.com/link/map/쿠시야끼하루,37.4960252005324,126.953228613056")
                .roadViewUrl("https://map.kakao.com/link/roadview/37.4960252005324,126.953228613056")
                .roadView(null)
                .categoryName("음식점 > 술집 > 일본식주점")
                .distance("105.0 m");

        String builderString = builder.toString();

        assertThat(builderString).contains("PubResponseBuilder");

        assertThat(builderString).contains("pubName=쿠시야끼하루 상도점");
        assertThat(builderString).contains("pubAddress=서울 동작구 상도동 490-5");
        assertThat(builderString).contains("directionUrl=https://map.kakao.com/link/map/쿠시야끼하루,37.4960252005324,126.953228613056");
        assertThat(builderString).contains("roadViewUrl=https://map.kakao.com/link/roadview/37.4960252005324,126.953228613056");
        assertThat(builderString).contains("roadView=null");
        assertThat(builderString).contains("categoryName=음식점 > 술집 > 일본식주점");
        assertThat(builderString).contains("distance=105.0 m");
    }

}