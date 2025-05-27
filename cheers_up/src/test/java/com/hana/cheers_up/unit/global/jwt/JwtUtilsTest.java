package com.hana.cheers_up.unit.global.jwt;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.global.config.clock.TimeProvider;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import com.hana.cheers_up.global.config.jwt.mac.MacProvider;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;
    @Mock
    private TimeProvider timeProvider;
    @Mock
    private MacProvider macProvider;


    private String secretKey = "testSecretKey1234567890987654321abcdefgh";

    private Long expiredMs = 3600000L;

    long currentTime;

    String userId;
    String nickname;
    String email;
    RoleType roleType;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtils, "expiredMs", expiredMs);

        currentTime = System.currentTimeMillis();
        userId = "kakao_1235467890";
        nickname = "공주하나";
        email = "hanana9506@naver.com";
        roleType = RoleType.USER;
    }


    @Test
    void 토큰생성에_성공한다() {
        //given

        //when
        String result = jwtUtils.generateToken(userId, nickname, email, roleType);

        //then
        assertThat(result).isNotNull();
        assertThat(result).startsWith("Bearer ");
    }


    @Test
    void 토큰_생성시_secretKey와_expiredMs가_없으면_예외가_발생한다() throws Exception{
        //given

        ReflectionTestUtils.setField(jwtUtils, "secretKey", null);
        ReflectionTestUtils.setField(jwtUtils, "expiredMs", null);

        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> jwtUtils.generateToken(userId, nickname, email, roleType));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.JWT_SECRET_KEY_NOT_CONFIGURED);
        assertThat(result.getMessage()).isEqualTo("key 혹은 expiredMs가 존재하지 않습니다.");
    }


    @Test
    void 토큰_생성시_secretKey가_없으면_예외가_발생한다() throws Exception{
        //given
        ReflectionTestUtils.setField(jwtUtils, "expiredMs", null);

        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> jwtUtils.generateToken(userId, nickname, email, roleType));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.JWT_SECRET_KEY_NOT_CONFIGURED);
        assertThat(result.getMessage()).isEqualTo("key 혹은 expiredMs가 존재하지 않습니다.");
    }


    @Test
    void 토큰_생성시_expiredMs가_없으면_예외가_발생한다() throws Exception{
        //given
        ReflectionTestUtils.setField(jwtUtils, "secretKey", null);

        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> jwtUtils.generateToken(userId, nickname, email, roleType));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.JWT_SECRET_KEY_NOT_CONFIGURED);
        assertThat(result.getMessage()).isEqualTo("key 혹은 expiredMs가 존재하지 않습니다.");
    }


    @Test
    void 토큰이_만료되었는지_검증한다_유효한경우() {
        //given
        given(timeProvider.getCurrentTime()).willReturn(currentTime);
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token.substring(7);

        //when
        boolean result = jwtUtils.isExpired(token);

        //then
        // 생성시점에 두번
        // 검증 시점에 한번
        then(timeProvider).should(times(3)).getCurrentTime();

        assertThat(result).isFalse();
    }


    @Test
    void 토큰이_만료되었는지_검증한다_만료된경우() {
        //given
        given(timeProvider.getCurrentTime()).willReturn(currentTime);
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token.substring(7);

        //다음번 시간 계산시 만료시간 이후로 설정
        given(timeProvider.getCurrentTime()).willReturn(currentTime + expiredMs + 1000L);

        //when
        boolean result = jwtUtils.isExpired(token);

        //then
        // 생성시점에 두번
        // 검증 시점에 한번
        then(timeProvider).should(times(3)).getCurrentTime();

        assertThat(result).isTrue();
    }


    @Test
    void jwt토큰을통해서_userId를_가져온다() {
        //given
        given(timeProvider.getCurrentTime()).willReturn(currentTime);
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token.substring(7);

        //when
        String result = jwtUtils.getUserId(token);

        //then
        then(timeProvider).should(times(2)).getCurrentTime();

        assertThat(result).isEqualTo(userId);
    }


    @Test
    void 유효한_토큰은_isInvalidated호출시_false를_반환한다() throws Exception{
        //given
        given(timeProvider.getCurrentTime()).willReturn(currentTime);
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token.substring(7);


        Mac mac = createMacInstance();
        given(macProvider.getMac(anyString(), any(SecretKeySpec.class))).willReturn(mac);


        //when
        boolean result = jwtUtils.isInvalidated(token); // 유효하지않음에 대한 검사이므로 return false

        //then
        then(timeProvider).should(times(2)).getCurrentTime();

        assertThat(result).isFalse();
    }


    @Test
    void 토큰의형식이_올바르지_않으면_예외가_발생한다() throws Exception {
        //given
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token.replaceAll(".","");

        String testToken = token;
        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> jwtUtils.isInvalidated(testToken));

        //then
        then(timeProvider).should(times(2)).getCurrentTime();

        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_TOKEN_FORMAT);
        assertThat(result.getMessage()).isEqualTo("토큰 형식이 올바르지 않습니다.");
    }


    @Test
    void 토큰을_복호화한값이_예상과_다르면_isInvalidated호출시_true를_반환한다() throws Exception{
        //given
        Mac mac = createMacInstance();
        given(macProvider.getMac(anyString(), any(SecretKeySpec.class))).willReturn(mac);

        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        token = token + "unexpectedToken";

        //when
        boolean result = jwtUtils.isInvalidated(token);

        //then
        then(timeProvider).should(times(2)).getCurrentTime();

        assertThat(result).isTrue();
    }


    @Test
    void 시그니처_생성중_예상하지못한_예외가_발생할_수_있다() throws Exception{
        //given
        String token = jwtUtils.generateToken(userId, nickname, email, roleType);
        given(macProvider.getMac(anyString(), any(SecretKeySpec.class)))
                .willThrow(new NoSuchAlgorithmException("알고리즘을 찾을 수 없습니다"));

        //when
        RuntimeException result = assertThrows(RuntimeException.class, () -> jwtUtils.isInvalidated(token));

        //then
        then(macProvider).should().getMac(anyString(), any(SecretKeySpec.class));

        assertThat(result.getMessage()).isEqualTo("시그니처 생성 중 오류 발생");
    }


    private Mac createMacInstance() throws Exception{
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        return mac;
    }
}

