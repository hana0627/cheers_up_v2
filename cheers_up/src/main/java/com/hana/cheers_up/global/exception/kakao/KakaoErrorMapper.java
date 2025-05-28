package com.hana.cheers_up.global.exception.kakao;

import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;

public class KakaoErrorMapper {

    /**
     * https://developers.kakao.com/docs/latest/ko/rest-api/reference#error-code
     */
    public static ApplicationException createKakaoError(KakaoErrorResponse kakaoError) {
        int code = kakaoError.getCode();
        String baseMessage = "ErrorCode[" + code + "] ";
        switch (code) {
            // 공통에러 - start
            case -1:
                // 서버 내부에서 처리 중에 에러가 발생한 경우
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "카카오 서버 내부 처리 중 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
            case -2:
                // 필수 인자가 포함되지 않은 경우나 호출 인자값의 데이타 타입이 적절하지 않거나 허용된 범위를 벗어난 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "요청 파라미터가 올바르지 않습니다.");
            case -3:
                // 해당 API를 사용하기 위해 필요한 기능(간편가입, 동의항목, 서비스 설정 등)이 활성화 되지 않은 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "해당 기능이 활성화되지 않았습니다.");
            case -4:
                // 계정이 제재된 경우나 해당 계정에 제재된 행동을 하는 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "계정이 제재되었거나 허용되지 않는 행동입니다.");
            case -5:
                // 해당 API에 대한 요청 권한이 없는 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "해당 API 사용 권한이 없습니다.");
            case -6:
                // 카카오 서비스에서 허용하지 않는 동작을 요청한 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "허용되지 않는 요청입니다.");
            case -7:
                // 서비스 점검 또는 내부 문제가 있는 경우
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "서비스 점검 중입니다. 잠시 후 다시 시도해주세요.");
            case -8:
                // 올바르지 않은 헤더로 요청한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "요청 헤더가 올바르지 않습니다.");
            case -9:
                // 서비스가 종료된 API를 호출한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "종료된 API입니다.");
            case -10:
                // 허용된 요청 회수를 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "API 호출 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
            case -401:
                // ** 유효하지 않은 앱키나 액세스 토큰으로 요청한 경우, 등록된 앱 정보와 호출된 앱 정보가 불일치 하는 경우 **
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "API 키가 유효하지 않거나 인증에 실패했습니다.");
            case -501:
                // 카카오톡 미가입 또는 유예 사용자가 카카오톡 또는 톡캘린더 API를 호출한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "카카오톡 미가입 사용자입니다.");
            case -602:
                // 이미지 업로드 시 최대 용량을 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "이미지 파일 크기가 너무 큽니다.");
            case -603:
                // 카카오 플랫폼 내부에서 요청 처리 중 타임아웃이 발생한 경우
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "요청 처리 시간이 초과되었습니다. 다시 시도해주세요.");
            case -606:
                // 업로드할 수 있는 최대 이미지 개수를 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "업로드 가능한 이미지 개수를 초과했습니다.");
            case -903:
                // 등록되지 않은 개발자의 앱키나 등록되지 않은 개발자의 앱키로 구성된 액세스 토큰으로 요청한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "등록되지 않은 개발자 앱키입니다.");
            case -911:
                // 	지원하지 않는 포맷의 이미지를 업로드 하는 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "지원하지 않는 이미지 형식입니다.");
            case -9798:
                // 서비스 점검중
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "서비스 점검 중입니다.");
            // 공통에러 - end
            // 카카오 로그인 - start
            case -101:
                // 해당 앱에 카카오계정 연결이 완료되지 않은 사용자가 호출한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "앱 연결이 완료되지 않은 사용자입니다.");
            case -102:
                // 	이미 앱과 연결되어 있는 사용자의 토큰으로 연결하기 요청한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "이미 연결된 사용자입니다.");
            case -103:
                // 휴면 상태, 또는 존재하지 않는 카카오계정으로 요청한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "존재하지 않거나 휴면 상태인 계정입니다.");
            case -201:
                // 사용자 정보 요청 API나 사용자 정보 저장 API 호출 시 앱에 추가하지 않은 사용자 프로퍼티 키 값을 불러오거나 저장하려고 한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "등록되지 않은 사용자 속성입니다.");
            case -402:
                // 해당 API에서 접근하는 리소스에 대해 사용자의 동의를 받지 않은 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "사용자 동의가 필요한 정보입니다.");
            case -406:
                // 14세 미만 미허용 설정이 되어 있는 앱으로 14세 미만 사용자가 API 호출한 경우
                return new ApplicationException(ErrorCode.KAKAO_UNAUTHORIZED_ERROR,
                        baseMessage + "14세 미만 사용자는 이용할 수 없습니다.");
            // 카카오 로그인 - end
            // 메시지 - start
            case -502:
                // 받는 이가 보내는 이의 친구가 아닌 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "친구 관계가 아닙니다.");
            case -530:
                // 받는 이가 메시지 수신 거부를 설정한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "메시지 수신을 거부한 사용자입니다.");
            case -532:
                // 특정 앱에서 보내는 이가 받는 사람 관계없이 하루 동안 보낼 수 있는 쿼터를 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "일일 메시지 발송 한도를 초과했습니다.");
            case -533:
                // 특정 앱에서 받는 이가 하루 동안 받을 수 있는 쿼터를 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "일일 메시지 수신 한도를 초과했습니다.");
            case -536:
                // '보내는 이와 받는 이' 한 쌍을 기준으로 하루 동안 주고 받을 수 있는 쿼터를 초과한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "일일 메시지 교환 한도를 초과했습니다.");
            // 메시지 - end
            // 카카오톡 채널 - start
            case -541:
                // 존재하지 않는 카카오톡 채널인 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "존재하지 않는 카카오톡 채널입니다.");
            case -544:
                // 카카오톡 채널이 제재 상태인 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "제재된 카카오톡 채널입니다.");
            case -815:
                // 카카오톡 채널 API 내부 에러
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "카카오톡 채널 API 내부 오류입니다.");
            case -816:
                // 파일 ID가 잘못된 경우나 해당 파일 ID로 업로드된 카카오톡 채널 고객파일을 찾을 수 없는 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "파일을 찾을 수 없습니다.");
            case -817:
                // 이미 존재하는 파일명이나 허용되지 않는 파일명으로 고객파일 등록하는 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "사용할 수 없는 파일명입니다.");
            case -818:
                // 등록한 고객파일이 최대 개수를 초과한 경우 (카카오톡 채널 관리자센터에 업로드한 파일 포함하여 최대 30개)
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "파일 업로드 한도를 초과했습니다.");
            case -819:
                // 	카카오톡 채널과 앱이 연결되지 않은 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "채널과 앱이 연결되지 않았습니다.");
            // 카카오톡 채널 - start
            // 푸시알림 & 톡캘린더 & 카카오모먼트 & 카카오 키워드 광고 - start
            case -901:
                // 	등록된 푸시 토큰이 없는 기기로 푸시 메시지를 보낸 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "등록된 푸시 토큰이 없습니다.");
            case -520:
                //공개 일정 ID 또는 캘린더 ID가 존재하지 않는 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "일정 또는 캘린더를 찾을 수 없습니다.");
            case -521:
                // 	카카오톡 프로필 스티커에 등록된 할 일을 수정 또는 삭제 시도한 경우
                return new ApplicationException(ErrorCode.KAKAO_CLIENT_ERROR,
                        baseMessage + "수정/삭제할 수 없는 할 일입니다.");
            case -813:
                // 카카오모먼트 API의 내부 에러
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "카카오모먼트 API 내부 오류입니다.");
            case -820:
                // 카카오 키워드광고 API의 내부 에러
                return new ApplicationException(ErrorCode.KAKAO_SERVER_ERROR,
                        baseMessage + "카카오 키워드광고 API 내부 오류입니다.");
            // 푸시알림 & 톡캘린더 & 카카오모먼트 & 카카오 키워드 광고 - end
            default:
                return new ApplicationException(ErrorCode.KAKAO_API_ERROR,
                        baseMessage + "카카오 API호출 중 예상하지 못한 예외가 발생했습니다.");
        }
    }
}
