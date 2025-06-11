package com.hana.cheers_up.unit.global.jwt;

import com.hana.cheers_up.global.config.jwt.JwtFilter;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import jakarta.servlet.FilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);

        // 각 테스트전에 SecurityContext초기화
        SecurityContextHolder.clearContext();

    }

    @Test
    void 루트경로_요청시_필터를_적용하지_않는다() throws Exception{
        //given
        request.setRequestURI("/");

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("/api/v1/ 로 시작하는 요청은 필터를 적용하지 않는다")
    @Test
    void v1로시작하는_url은_필터를_적용하지_않는다() throws Exception{
        //given
        request.setRequestURI("/api/v1/users/login");

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("루트 요청과 /api/v1/ 로 시작하지않는 요청은 필터를 적용한다")
    @Test
    void v1로시작하지않는_url은_필터를_적용한다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    void 이미_인증된_사용자는_필터를_적용하지_않는다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");
        Authentication auth = mock(Authentication.class);

        given(auth.isAuthenticated()).willReturn(true);

        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(true);

    }


    @Test
    void 익명인증토큰_사용자는_필터를_적용한다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");
        Authentication auth = mock(AnonymousAuthenticationToken.class);

        given(auth.isAuthenticated()).willReturn(true);

        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void 인증정보가_없는_사용자는_필터를_적용한다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");

        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void 인증되지_않은_사용자는_필터를_적용한다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");
        Authentication auth = mock(Authentication.class);
        given(auth.isAuthenticated()).willReturn(false);

        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);


        //when
        boolean result = jwtFilter.shouldNotFilter(request);

        //then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void 유효한_jwt_토큰이_존재하면_인증절차를_수행한다() throws Exception{
        //given
        String testToken = "thisistesttoken";
        String userId = "kakao_1234567890";
        request.setRequestURI("/api/v2/search");
        request.addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + testToken);

        given(jwtUtils.isInvalidated(testToken)).willReturn(false);
        given(jwtUtils.isExpired(testToken)).willReturn(false);
        given(jwtUtils.getUserId(testToken)).willReturn(userId);

        //when
        jwtFilter.doFilterInternal(request, response, filterChain);

        //then
        then(jwtUtils).should().isInvalidated(testToken);
        then(jwtUtils).should().isExpired(testToken);
        then(jwtUtils).should().getUserId(testToken);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo(userId);

    }

    @Test
    void Authentication_헤더가_없으면_예외가_발생한다() throws Exception{
        //given
        request.setRequestURI("/api/v2/search");

        //when
        jwtFilter.doFilterInternal(request, response, filterChain);

        //then
        then(filterChain).shouldHaveNoInteractions();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        String responseBody = response.getContentAsString();
        assertThat(responseBody).contains("UNAUTHORIZED");
        assertThat(responseBody).contains("로그인이 필요한 서비스입니다.");
    }

    @Test
    void 토큰형식이_Bearer로_시작하지_않으면_예외가_발생한다() throws Exception{
        //given
        String testToken = "thisistesttoken";
        String userId = "kakao_1234567890";
        request.setRequestURI("/api/v2/search");
        request.addHeader(HttpHeaders.AUTHORIZATION,"PRETTY " + testToken);

        //when
        jwtFilter.doFilterInternal(request, response, filterChain);

        //then
        then(filterChain).shouldHaveNoInteractions();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        String responseBody = response.getContentAsString();
        assertThat(responseBody).contains("UNAUTHORIZED");
        assertThat(responseBody).contains("로그인이 필요한 서비스입니다.");


    }


    @Test
    void 유효하지_않는_jwt_토큰으로_요청시_로그인페이지로_redirect한다() throws Exception{
        //given
        String testToken = "thisistesttoken";
        String userId = "kakao_1234567890";
        request.setRequestURI("/api/v2/search");
        request.addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + testToken);

        given(jwtUtils.isInvalidated(testToken)).willReturn(true);

        //when
        jwtFilter.doFilterInternal(request, response, filterChain);

        //then
        then(jwtUtils).should().isInvalidated(testToken);
        then(jwtUtils).shouldHaveNoMoreInteractions();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

    }

    @Test
    void jwt_토큰이_만료되었다면_로그인페이지로_redirect한다() throws Exception{
        //given
        String testToken = "thisistesttoken";
        String userId = "kakao_1234567890";
        request.setRequestURI("/api/v2/search");
        request.addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + testToken);

        given(jwtUtils.isInvalidated(testToken)).willReturn(false);
        given(jwtUtils.isExpired(testToken)).willReturn(true);

        //when
        jwtFilter.doFilterInternal(request, response, filterChain);

        //then
        then(jwtUtils).should().isInvalidated(testToken);
        then(jwtUtils).should().isExpired(testToken);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

    }

}

