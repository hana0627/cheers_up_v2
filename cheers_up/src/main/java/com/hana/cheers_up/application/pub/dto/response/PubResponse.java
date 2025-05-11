package com.hana.cheers_up.application.pub.dto.response;

import com.hana.cheers_up.application.pub.domain.Direction;
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

    public static PubResponse from(Direction direction, String directionUrl, String roadViewUrl) {
        return PubResponse.builder()
                .pubName(direction.getTargetPubName())
                .pubAddress(direction.getTargetAddress())
                .directionUrl(directionUrl)
                .roadViewUrl(roadViewUrl)
                .categoryName(direction.getTargetCategoryName())
                .distance(String.format("%.1f m", direction.getDistance()))
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
