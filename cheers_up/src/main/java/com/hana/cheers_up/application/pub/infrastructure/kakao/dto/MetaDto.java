package com.hana.cheers_up.application.pub.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaDto(
        @JsonProperty("total_count")
        Integer totalCount,
        @JsonProperty("pageable_count")
        Long pageCount,
        @JsonProperty("is_end")
        Boolean isEnd


) {
}
