package com.hana.cheers_up.application.user.service;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    @Transactional
    public UserAccountDto saveUser(UserAccountDto dto) {
        log.info("[UserAccountService saveUser]");
        return UserAccountDto.from(userAccountRepository.save(dto.toEntity()));
    }

    /**
     * Optional로 반환하는 이유 -> 23.04.18 회원 여부를 파악하여, SecurityConfig에서 추가 작업을 수행.
     * 그 과정에서 method chainning이 일어나고, 그 chainning이 끝나는 시점에서 객체를 반환하는 시직으로 작업하였음
     */
    public Optional<UserAccountDto> searchUser(String username) {
        log.info("[UserAccountService searchUser]");
        return userAccountRepository.findById(username).map(UserAccountDto::from);
    }
}
