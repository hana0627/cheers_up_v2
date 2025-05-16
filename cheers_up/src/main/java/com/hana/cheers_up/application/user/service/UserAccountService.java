package com.hana.cheers_up.application.user.service;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.repository.UserAccountRepository;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import com.hana.cheers_up.global.config.oauth2.provider.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final JwtUtils jwtUtils;

    public String login(UserAccountDto userAccountDto) {
        UserAccount foundUser = userAccountRepository.findById(userAccountDto.userId()).orElseThrow(() -> new EntityNotFoundException());

        // 토큰생성
        return jwtUtils.generateToken(
                foundUser.getUserId(),
                foundUser.getNickname(),
                foundUser.getEmail(),
                foundUser.getRoleType()
        );

    }


    /**
     * Optional로 반환하는 이유 -> 23.04.18 회원 여부를 파악하여, SecurityConfig에서 추가 작업을 수행.
     * 그 과정에서 method chainning이 일어나고, 그 chainning이 끝나는 시점에서 객체를 반환하는 시직으로 작업하였음
     */
//    public UserAccountDto searchUser(String username) {
//        log.info("[UserAccountService searchUser]");
//        return userAccountRepository.findById(username).map(UserAccountDto::from)
//                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다 - username : " + username));
//    }

    @Transactional
    public UserAccountDto searchUserOrSave(String username, Oauth2UserInfo oauth2UserInfo) {
        return userAccountRepository.findById(username).map(UserAccountDto::from)
                .orElseGet(() -> {
                    UserAccount saved = userAccountRepository.save(
                            UserAccount.of(username, oauth2UserInfo.getEmail(), oauth2UserInfo.getNickname(), null, RoleType.USER)
                    );
                    return UserAccountDto.from(saved);
                });
    }
}
