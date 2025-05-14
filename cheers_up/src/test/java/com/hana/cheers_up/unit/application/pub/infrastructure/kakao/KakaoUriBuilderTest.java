package com.hana.cheers_up.unit.application.pub.infrastructure.kakao;

import com.hana.cheers_up.application.pub.infrastructure.kakao.KakaoUriBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KakaoUriBuilderTest {

    @InjectMocks
    private KakaoUriBuilder kakaoUriBuilder;
    private static final String PUB_CATEGORY = "FD6";
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";

    @Test
    void 주소를_좌표로_변환하는_URI를_생성한다() {
        // given
        String address = "서울 동작구 상도로 357";


        URI expectedUri = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();

        // when
        URI result = kakaoUriBuilder.buildUriByAddressSearch(address);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedUri);
    }

    @Test
    void 카테고리로_장소를_검색하는_URI를_생성한다() {
        //given
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;


        URI expectedUri = UriComponentsBuilder.fromHttpUrl(KAKAO_CATEGORY_SEARCH_URL)
                .queryParam("category_group_code", PUB_CATEGORY)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius * 1000)
                .queryParam("sort", "distance")
                .queryParam("page", 1)
                .build()
                .encode()
                .toUri();


        //when
        URI result = kakaoUriBuilder.buildUriByCategorySearch(latitude, longitude, radius, PUB_CATEGORY, 1);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedUri);

    }
}
