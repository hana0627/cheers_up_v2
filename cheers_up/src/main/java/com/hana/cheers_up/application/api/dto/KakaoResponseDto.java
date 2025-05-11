package com.hana.cheers_up.application.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoResponseDto(
        @JsonProperty("meta")
        MetaDto metaDto,
        @JsonProperty("documents")

        List<DocumentDto> documentDtos
) {

}
