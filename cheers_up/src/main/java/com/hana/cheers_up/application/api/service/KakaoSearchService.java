package com.hana.cheers_up.application.api.service;

import com.hana.cheers_up.application.api.dto.KakaoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoSearchService {
    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    private static final String PUB_CATEGORY = "FD6";

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;


    public KakaoResponseDto requestAddressSearch(String address) {
        if (ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        //kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponseDto.class).getBody();
    }

    public KakaoResponseDto requestPubCategorySearch(double latitude, double longitude, double radius, int page) {
        URI uri = kakaoUriBuilderService.buildUriByCategorySearch(latitude, longitude, radius, PUB_CATEGORY, page);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponseDto.class).getBody();

    }
}
