package com.hana.cheers_up.application.pub.infrastructure.kakao;

import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoSearch {
    private final RestTemplate restTemplate;
    private final KakaoUriBuilder kakaoUriBuilder;

    private static final String PUB_CATEGORY = "FD6";

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;


    /**
     * input : 주소정보
     * output : 좌표
     * @param address
     * @return
     */
    public KakaoResponse getCoordinatesByAddress(String address) {
        if (ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilder.buildUriByAddressSearch(address);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        //kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class).getBody();
    }


    public KakaoResponse getRestaurantsByLocation(double latitude, double longitude, double radius, int page) {
        URI uri = kakaoUriBuilder.buildUriByCategorySearch(latitude, longitude, radius, PUB_CATEGORY, page);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class).getBody();

    }
}
