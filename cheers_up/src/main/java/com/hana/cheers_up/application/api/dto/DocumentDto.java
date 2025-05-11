package com.hana.cheers_up.application.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record DocumentDto(
        /**
         "address_name": "서울 서대문구 남가좌동 385",
         "category_group_code": "FD6",
         "category_group_name": "음식점",
         "category_name": "음식점 > 술집 > 호프,요리주점 > 인쌩맥주",
         "distance": "65",
         "id": "1950045171",
         "phone": "",
         "place_name": "인쌩맥주 가재울점",
         "place_url": "http://place.map.kakao.com/1950045171",
         "road_address_name": "서울 서대문구 가재울미래로 2",
         "x": "126.91722692752693",
         "y": "37.574176266133975"
         */
        @JsonProperty("place_name")
        String placeName, // 사용안하는데 혹시모름
        @JsonProperty("address_name")
        String addressName,
        @JsonProperty("y")
        double latitude,
        @JsonProperty("x")
        double longitude,
        @JsonProperty("distance")
        double distance,
        @JsonProperty("category_name")
        String categoryName
) {


}
