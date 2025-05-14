package com.hana.cheers_up.unit.application.pub.infrastructure.kakao;


import com.hana.cheers_up.application.pub.infrastructure.kakao.KakaoSearch;
import com.hana.cheers_up.application.pub.infrastructure.kakao.KakaoUriBuilder;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.DocumentDto;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.MetaDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
//@TestPropertySource(properties = "kakao.rest.api.key=kakaoRestApiKey")
class KakaoSearchTest {

    @InjectMocks
    private KakaoSearch kakaoSearch;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;


    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KakaoUriBuilder kakaoUriBuilder;

    private static final String PUB_CATEGORY = "FD6";
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";


    // 주소를 통한 좌표 검색 메서드 테스트
    @Test
    void getCoordinatesByAddressTest() {
        // given
        String address = "서울 동작구 상도로 357";
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
//        kakaoSearch.kakaoRestApiKey = "kakaoRestApiKey";

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        MetaDto coordiateMetaDto = new MetaDto(1, 1L, true);
        List<DocumentDto> coordinateDocumentDtoList = List.of(new DocumentDto(null, address, latitude, longitude, 0.0, null));
        KakaoResponse coordinate = new KakaoResponse(coordiateMetaDto, coordinateDocumentDtoList);


        given(kakaoUriBuilder.buildUriByAddressSearch(address)).willReturn(uri);
        given(restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class))
                .willReturn(new ResponseEntity<>(coordinate, HttpStatus.OK));

        // when
        KakaoResponse result = kakaoSearch.getCoordinatesByAddress(address);

        // then
        then(kakaoUriBuilder).should().buildUriByAddressSearch(address);
        then(restTemplate).should().exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class);
        assertThat(result).isEqualTo(coordinate);
        assertThat(result.metaDto()).isEqualTo(coordiateMetaDto);
        assertThat(result.documentDtos()).isEqualTo(coordinateDocumentDtoList);
    }

    // 좌표를 통한 음식점 검색 메서드 테스트
    @Test
    void getRestaurantsByLocationTest() {
        // given
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;
//        kakaoSearch.kakaoRestApiKey = "kakaoRestApiKey";

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_CATEGORY_SEARCH_URL)
                .queryParam("category_group_code", PUB_CATEGORY)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius * 1000)
                .queryParam("sort", "distance")
                .queryParam("page", 1)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        MetaDto restaurantsMetaDto = new MetaDto(7206, 45L, false);
        List<DocumentDto> restaurantsDocumentList = getRestaurantsDocumentList();
        KakaoResponse restaurants = new KakaoResponse(restaurantsMetaDto, restaurantsDocumentList);


        given(kakaoUriBuilder.buildUriByCategorySearch(latitude, longitude, radius, PUB_CATEGORY, 1)).willReturn(uri);
        given(restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class))
                .willReturn(new ResponseEntity<>(restaurants, HttpStatus.OK));

        // when
        KakaoResponse result = kakaoSearch.getRestaurantsByLocation(latitude, longitude, radius, 1);

        // then
        then(kakaoUriBuilder).should().buildUriByCategorySearch(latitude, longitude, radius, PUB_CATEGORY, 1);
        then(restTemplate).should().exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class);

        assertThat(result).isEqualTo(restaurants);
        assertThat(result.metaDto()).isEqualTo(restaurantsMetaDto);
        assertThat(result.documentDtos()).isEqualTo(restaurantsDocumentList);
    }


    private static List<DocumentDto> getRestaurantsDocumentList() {
        return List.of(
                new DocumentDto("자월당", "서울 동작구 상도동 492-13", 37.4958909800973, 126.953303331816, 118.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("김밥이맛있는집", "서울 동작구 상도동 489-7", 37.495873988371, 126.95362676089, 118.0, "음식점 > 분식"),
                new DocumentDto("벙커", "서울 동작구 상도동 489-7", 37.49586317007792, 126.95361093590714, 119.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("파동추야", "서울 동작구 상도동 490-6", 37.4959025828941, 126.953025139347, 123.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("황새골", "서울 동작구 상도동 477-17", 37.4979243653182, 126.952833887642, 125.0, "음식점 > 한식"),
                new DocumentDto("뚜레쥬르 상도래미안점", "서울 동작구 상도동 477-17", 37.4979315787846, 126.952847453484, 125.0, "음식점 > 간식 > 제과,베이커리 > 뚜레쥬르"),
                new DocumentDto("79대포 숭실대점", "서울 동작구 상도동 489-8", 37.4958037271631, 126.953670906816, 126.0, "음식점 > 술집 > 실내포장마차 > 79대포"),
                new DocumentDto("맥주창고YOLO", "서울 동작구 상도동 489-8", 37.4958091549262, 126.953726314276, 126.0, "음식점 > 술집"),
                new DocumentDto("상도동솥뚜껑 숭실대점", "서울 동작구 상도동 492-14", 37.4957801911274, 126.953391605732, 129.0, "음식점 > 한식 > 육류,고기 > 삼겹살"),
                new DocumentDto("브레댄코 숭실대점", "서울 동작구 상도동 514", 37.49581469115424, 126.95405990659631, 133.0, "음식점 > 간식 > 제과,베이커리 > 브레댄코"),
                new DocumentDto("햇살아래테이블", "서울 동작구 상도동 477-16", 37.498003626748, 126.952765985829, 136.0, "음식점 > 치킨"),
                new DocumentDto("교촌치킨 숭실대점", "서울 동작구 상도동 489-9", 37.4957199579422, 126.9537331543, 136.0, "음식점 > 치킨 > 교촌치킨"),
                new DocumentDto("롯데리아 숭실대입구역점", "서울 동작구 상도동 477-15", 37.49792878209975, 126.95261336621236, 136.0, "음식점 > 패스트푸드 > 롯데리아"),
                new DocumentDto("맛닭꼬 숭실대입구역점", "서울 동작구 상도동 477-16", 37.4980108329882, 126.952761457826, 137.0, "음식점 > 치킨 > 맛닭꼬"),
                new DocumentDto("이모이모", "서울 동작구 상도동 489-10", 37.49571273978382, 126.95375566570743, 137.0, "음식점 > 술집 > 호프,요리주점")
        );
    }


}
