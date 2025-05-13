package com.hana.cheers_up.application.pub.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoResponse(
        // metaDto=MetaDto[totalCount=1, pageCount=1, isEnd=true
        @JsonProperty("meta")
        MetaDto metaDto,
        // documentDtos=[DocumentDto[placeName=null, addressName=서울 동작구 상도로 357, latitude=37.4969397553084, longitude=126.953540835787, distance=0.0, categoryName=null]]
        @JsonProperty("documents")
        List<DocumentDto> documentDtos
) {

}
