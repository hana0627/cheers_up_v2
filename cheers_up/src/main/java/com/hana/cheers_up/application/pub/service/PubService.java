package com.hana.cheers_up.application.pub.service;

import com.hana.cheers_up.application.api.dto.DocumentDto;
import com.hana.cheers_up.application.api.dto.KakaoResponseDto;
import com.hana.cheers_up.application.api.service.KakaoSearchService;
import com.hana.cheers_up.application.pub.domain.Direction;
import com.hana.cheers_up.application.pub.dto.PubDto;
import com.hana.cheers_up.application.pub.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PubService {

    private final PubRepository pubRepository;

    public List<PubDto> Pubs() {
        return pubRepository.findAll().stream()
                .map(PubDto::from).toList();
    }


}
