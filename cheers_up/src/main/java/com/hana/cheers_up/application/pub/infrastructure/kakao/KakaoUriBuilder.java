package com.hana.cheers_up.application.pub.infrastructure.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class KakaoUriBuilder {

    private static final String KAKAO_SEARCH_KEYWORD = "맥주";
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public URI buildUriByAddressSearch(String address) {

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
                .queryParam("query",address)
                .build().encode().toUri();

        log.info("[KakaoUriBuilderService buildUriByAddressSearch] address: {}, uri: {}", address, uri);
        return uri;

    }

    public URI buildUriByKeywordSearch(double latitude, double longitude, double radius, String category, int page) {
        double meterRadius = radius* 1000;
        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_KEYWORD_SEARCH_URL)
                .queryParam("query",KAKAO_SEARCH_KEYWORD)
                .queryParam("category_group_code", category)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", meterRadius)
                .queryParam("sort","distance")
                .queryParam("page",page)
                .build().encode().toUri();

        log.info("[KakaoUriBuilderService buildUriByKeywordSearch] uri: {}", uri);
        return uri;
    }
}
