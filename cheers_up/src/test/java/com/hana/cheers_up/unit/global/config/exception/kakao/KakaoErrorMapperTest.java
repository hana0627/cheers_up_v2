package com.hana.cheers_up.unit.global.config.exception.kakao;

import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import com.hana.cheers_up.global.exception.kakao.KakaoErrorMapper;
import com.hana.cheers_up.global.exception.kakao.KakaoErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class KakaoErrorMapperTest {

    @ParameterizedTest
    @DisplayName("카카오 응답 예외처리 테스트")
    @CsvSource({
            "-1, KAKAO_SERVER_ERROR, '카카오 서버 내부 처리 중 에러가 발생했습니다. 잠시 후 다시 시도해주세요.'",
            "-2, KAKAO_CLIENT_ERROR, '요청 파라미터가 올바르지 않습니다'",
            "-3, KAKAO_UNAUTHORIZED_ERROR, '해당 기능이 활성화되지 않았습니다.'",
            "-4, KAKAO_UNAUTHORIZED_ERROR, '계정이 제재되었거나 허용되지 않는 행동입니다.'",
            "-5, KAKAO_UNAUTHORIZED_ERROR, '해당 API 사용 권한이 없습니다.'",
            "-6, KAKAO_UNAUTHORIZED_ERROR, '허용되지 않는 요청입니다.'",
            "-7, KAKAO_SERVER_ERROR, '서비스 점검 중입니다. 잠시 후 다시 시도해주세요.'",
            "-8, KAKAO_CLIENT_ERROR, '요청 헤더가 올바르지 않습니다.'",
            "-9, KAKAO_CLIENT_ERROR, '종료된 API입니다.'",
            "-10, KAKAO_CLIENT_ERROR, 'API 호출 한도를 초과했습니다. 잠시 후 다시 시도해주세요.'",

            "-401, KAKAO_UNAUTHORIZED_ERROR, 'API 키가 유효하지 않거나 인증에 실패했습니다.'",
            "-501, KAKAO_CLIENT_ERROR, '카카오톡 미가입 사용자입니다.'",
            "-602, KAKAO_CLIENT_ERROR, '이미지 파일 크기가 너무 큽니다.'",
            "-603, KAKAO_SERVER_ERROR, '요청 처리 시간이 초과되었습니다. 다시 시도해주세요.'",
            "-606, KAKAO_CLIENT_ERROR, '업로드 가능한 이미지 개수를 초과했습니다.'",
            "-903, KAKAO_CLIENT_ERROR, '등록되지 않은 개발자 앱키입니다.'",
            "-911, KAKAO_CLIENT_ERROR, '지원하지 않는 이미지 형식입니다.'",
            "-9798, KAKAO_SERVER_ERROR, '서비스 점검 중입니다.'",

            "-101, KAKAO_CLIENT_ERROR, '앱 연결이 완료되지 않은 사용자입니다.'",
            "-102, KAKAO_CLIENT_ERROR, '이미 연결된 사용자입니다.'",
            "-103, KAKAO_CLIENT_ERROR, '존재하지 않거나 휴면 상태인 계정입니다.'",
            "-201, KAKAO_CLIENT_ERROR, '등록되지 않은 사용자 속성입니다.'",
            "-402, KAKAO_UNAUTHORIZED_ERROR, '사용자 동의가 필요한 정보입니다.'",
            "-406, KAKAO_UNAUTHORIZED_ERROR, '14세 미만 사용자는 이용할 수 없습니다.'",
            "-502, KAKAO_CLIENT_ERROR, '친구 관계가 아닙니다.'",
            "-530, KAKAO_CLIENT_ERROR, '메시지 수신을 거부한 사용자입니다.'",
            "-532, KAKAO_CLIENT_ERROR, '일일 메시지 발송 한도를 초과했습니다.'",
            "-533, KAKAO_CLIENT_ERROR, '일일 메시지 수신 한도를 초과했습니다.'",
            "-536, KAKAO_CLIENT_ERROR, '일일 메시지 교환 한도를 초과했습니다.'",

            "-541, KAKAO_CLIENT_ERROR, '존재하지 않는 카카오톡 채널입니다.'",
            "-544, KAKAO_CLIENT_ERROR, '제재된 카카오톡 채널입니다.'",
            "-815, KAKAO_SERVER_ERROR, '카카오톡 채널 API 내부 오류입니다.'",
            "-816, KAKAO_CLIENT_ERROR, '파일을 찾을 수 없습니다.'",
            "-817, KAKAO_CLIENT_ERROR, '사용할 수 없는 파일명입니다.'",
            "-818, KAKAO_CLIENT_ERROR, '파일 업로드 한도를 초과했습니다.'",
            "-819, KAKAO_CLIENT_ERROR, '채널과 앱이 연결되지 않았습니다.'",


            "-901, KAKAO_CLIENT_ERROR, '등록된 푸시 토큰이 없습니다.'",
            "-520, KAKAO_CLIENT_ERROR, '일정 또는 캘린더를 찾을 수 없습니다.'",
            "-521, KAKAO_CLIENT_ERROR, '수정/삭제할 수 없는 할 일입니다.'",
            "-813, KAKAO_SERVER_ERROR, '카카오모먼트 API 내부 오류입니다.'",
            "-820, KAKAO_SERVER_ERROR, '카카오 키워드광고 API 내부 오류입니다.'",

            "-0, KAKAO_API_ERROR, '카카오 API호출 중 예상하지 못한 예외가 발생했습니다.'",
    })
    void createKakaoError_test(int errorCode, ErrorCode expectedErrorCode, String expectedMessagePart) {
        // given
        KakaoErrorResponse kakaoError = new KakaoErrorResponse(errorCode, "Test message");

        // when
        ApplicationException result = KakaoErrorMapper.createKakaoError(kakaoError);

        // then
        assertThat(result.getErrorCode()).isEqualTo(expectedErrorCode);
        assertThat(result.getMessage()).contains("ErrorCode[" + errorCode + "]");
        assertThat(result.getMessage()).contains(expectedMessagePart);
    }


    @Test
    void 기본생성자_테스트() {
        //given
        KakaoErrorMapper mapper = new KakaoErrorMapper();

        //when

        //then

    }

}
