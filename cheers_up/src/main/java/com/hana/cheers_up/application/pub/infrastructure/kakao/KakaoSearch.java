package com.hana.cheers_up.application.pub.infrastructure.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import com.hana.cheers_up.global.exception.kakao.KakaoErrorMapper;
import com.hana.cheers_up.global.exception.kakao.KakaoErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoSearch {
    private final RestTemplate restTemplate;
    private final KakaoUriBuilder kakaoUriBuilder;
    private final ObjectMapper objectMapper;

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

        URI uri = kakaoUriBuilder.buildUriByAddressSearch(address);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        try {
            KakaoResponse result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class).getBody();
            if (result == null) {
                log.error("[카카오 좌표검색] : result is null, requestAddress: {}", address);
                throw new ApplicationException(ErrorCode.KAKAO_RESPONSE_EMPTY, "카카오 좌표검색 결과가 없습니다.");
            }
            return result;
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // 1. 카카오 예외처리 받기
            KakaoErrorResponse errorResponse = parseKakaoError(e);

            log.error("[카카오 좌표검색] : API 에러 HttpStatus:{}, KakaoCode:{}, KakaoMessage:{}, requestAddress:{}", e.getStatusCode(), errorResponse.getCode(), errorResponse.getMsg(), address);

            throw KakaoErrorMapper.createKakaoError(errorResponse);

        } catch (ResourceAccessException e) {
            log.error("[카카오 좌표검색] : 네트워크 연결 실패 errorMessage:{}, requestAddress:{}", e.getMessage(), address);
            throw new ApplicationException(ErrorCode.NETWORK_ERROR, "카카오 API 네트워크 연결 실패");

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[카카오 좌표검색] : 예상치 못한 에러 errorMessage:{}, requestAddress:{}", e.getMessage(), address);
            throw new ApplicationException(ErrorCode.KAKAO_API_ERROR, ErrorCode.KAKAO_API_ERROR.getMessage());
        }
    }


    public KakaoResponse getRestaurantsByLocation(double latitude, double longitude, double radius, int page) {
        URI uri = kakaoUriBuilder.buildUriByKeywordSearch(latitude, longitude, radius, PUB_CATEGORY, page);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        try {
            KakaoResponse result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponse.class).getBody();
            return result;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 1. 카카오 예외처리 받기
            KakaoErrorResponse errorResponse = parseKakaoError(e);

            log.error("[카카오 키워드검색] : API 에러 HttpStatus:{}, KakaoCode:{}, KakaoMessage:{}, requestLatitude:{}, requestLongitude:{}", e.getStatusCode(), errorResponse.getCode(), errorResponse.getMsg(), latitude, longitude);

            throw KakaoErrorMapper.createKakaoError(errorResponse);

        } catch (ResourceAccessException e) {
            log.error("[카카오 키워드검색] : 네트워크 연결 실패 errorMessage:{}, requestLatitude:{}, requestLongitude:{}", e.getMessage(), latitude, longitude);
            throw new ApplicationException(ErrorCode.NETWORK_ERROR, "카카오 API 네트워크 연결 실패");

        } catch (Exception e) {
            log.error("[카카오 키워드검색] : 예상치 못한 에러 errorMessage:{}, requestLatitude:{}, requestLongitude:{}", e.getMessage(), latitude, longitude);
            throw new ApplicationException(ErrorCode.KAKAO_API_ERROR, "카카오 API호출 중 예상하지 못한 예외가 발생했습니다.");
        }
    }

    /**
     * 카카오 API에러 파싱
     * https://developers.kakao.com/docs/latest/ko/rest-api/reference#error-code
     */
    private KakaoErrorResponse parseKakaoError(HttpStatusCodeException e) {
        try {
            String responseBody = e.getResponseBodyAsString();
            if (!responseBody.isEmpty()) {
                return objectMapper.readValue(responseBody, KakaoErrorResponse.class);
            }
            throw new ApplicationException(ErrorCode.KAKAO_API_ERROR, "카카오 API호출 중 예상하지 못한 예외가 발생했습니다.");
        } catch (Exception parseException) {
            log.warn("[카카오 에러 파싱] : 에러 응답 파싱 실패 - {}, kakaoCode :{}, kakaoMessage :{}", parseException.getMessage(), e.getStatusCode(), e.getMessage());
            throw new ApplicationException(ErrorCode.KAKAO_API_ERROR, "카카오 API호출 중 예상하지 못한 예외가 발생했습니다.");
        }
    }
}
