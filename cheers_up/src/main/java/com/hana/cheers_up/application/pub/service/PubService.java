package com.hana.cheers_up.application.pub.service;

import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.DocumentDto;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse;
import com.hana.cheers_up.application.pub.infrastructure.kakao.KakaoSearch;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.LocationSearchResult;
import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PubService {
    // 3키로 내외
    private static final double RADIUS_KM = 3.0;
    private static final String PUB_CATEGORY = "술집";
    private final KakaoSearch kakaoSearch;
    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @Transactional
    public List<PubResponse> recommendPubs(String address) {
        log.info("[DirectionService recommendPubs]");
        if (ObjectUtils.isEmpty(address)) return Collections.emptyList();

        // step1. 주소를 통한  좌표계산
        // [
        // metaDto=MetaDto[totalCount=1, pageCount=1, isEnd=true],
        // documentDtos=[DocumentDto[placeName=null, addressName=서울 동작구 상도로 357, latitude=37.4969397553084, longitude=126.953540835787, distance=0.0, categoryName=null]]
        // ]
        KakaoResponse kakaoResponse = kakaoSearch.getCoordinatesByAddress(address);

        if (ObjectUtils.isEmpty(kakaoResponse) || CollectionUtils.isEmpty(kakaoResponse.documentDtos())) {
            log.error("[PubService recommendPubs fail] Input address: {}", address);
            return Collections.emptyList();
        }

        // step2. 좌표를 통해 가까운 식당 목록 조회
        DocumentDto documentDto = kakaoResponse.documentDtos().get(0);
        List<LocationSearchResult> locationSearchResults = getRestaurants(documentDto);

        // step3. 검색결과 필터링 및 길찾기, 로드뷰 url 추가
        // "category_name" 중 "술집"이 포함된 데이터 추출
        return locationSearchResults.stream()
                .map(locationSearchResult -> {
                    // 길찾기 주소정보
                    String directionUrl = createDirectionUrl(locationSearchResult);
                    // 로드뷰 주소정보
                    String roadViewUrl = ROAD_VIEW_BASE_URL + locationSearchResult.targetLatitude() + "," + locationSearchResult.targetLongitude();
                    return PubResponse.from(locationSearchResult, directionUrl, roadViewUrl);
                })
                .filter(pubResponse -> pubResponse.categoryName().contains(PUB_CATEGORY)).toList();
    }



    private List<LocationSearchResult> getRestaurants(DocumentDto inputDto) {
        List<LocationSearchResult> locationSearchResults = new ArrayList<>();
        int page = 0;

        KakaoResponse kakaoResponse;

        do {
            page++;
            kakaoResponse = kakaoSearch.getRestaurantsByLocation(inputDto.latitude(), inputDto.longitude(), RADIUS_KM, page); // api -call
            addDirections(inputDto, locationSearchResults, kakaoResponse); // 결과저장
            // 결과값이 끝이거나, 예상외로 결과값이 너무 많을경우 20번만 반복하는것으로 제한
            // 새로 알게된 사실 -- kakao 지도 api는 최대 45개의 정보밖에 제공하지 않는다.
        } while (!kakaoResponse.metaDto().isEnd() && page < 3);

        return locationSearchResults;
    }

    private static void addDirections(DocumentDto inputDto, List<LocationSearchResult> locationSearchResults, KakaoResponse kakaoResponse) {
        locationSearchResults.addAll(kakaoResponse
                .documentDtos().stream()
                .map(pubDto -> LocationSearchResult.from(inputDto, pubDto))
                .toList());
    }

    private static String createDirectionUrl(LocationSearchResult locationSearchResult) {
        String params = String.join(",", locationSearchResult.targetPubName(),
                String.valueOf(locationSearchResult.targetLatitude()), String.valueOf(locationSearchResult.targetLongitude()));

        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();
        return directionUrl;
    }


}
