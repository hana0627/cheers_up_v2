package com.hana.cheers_up.application.pub.controller.response;

import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.LocationSearchResult;
import lombok.Builder;


public record PubResponse(

        String pubName,
        String pubAddress,
        String directionUrl,
        String roadViewUrl,
        String roadView,
        String categoryName,
        String distance
) {

    public static PubResponse from(LocationSearchResult locationSearchResult, String directionUrl, String roadViewUrl) {
        return PubResponse.builder()
                .pubName(locationSearchResult.targetPubName())
                .pubAddress(locationSearchResult.targetAddress())
                .directionUrl(directionUrl)
                .roadViewUrl(roadViewUrl)
                .categoryName(locationSearchResult.targetCategoryName())
                .distance(String.format("%.1f m", locationSearchResult.distance()))
                .build();
    }


    @Builder
    public PubResponse(String pubName, String pubAddress, String directionUrl, String roadViewUrl, String roadView, String categoryName, String distance) {
        this.pubName = pubName;
        this.pubAddress = pubAddress;
        this.directionUrl = directionUrl;
        this.roadViewUrl = roadViewUrl;
        this.roadView = roadView;
        this.categoryName = categoryName;
        this.distance = distance;
    }
}
