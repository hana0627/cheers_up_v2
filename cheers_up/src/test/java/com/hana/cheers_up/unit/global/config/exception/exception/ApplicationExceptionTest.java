package com.hana.cheers_up.unit.global.config.exception.exception;

import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationExceptionTest {

    @Test
    void ErrorCode의_메세지가_없으면_기본_ErrorCode의_메세지를_제공한다() throws Exception{
        //given & when
        ApplicationException result = new ApplicationException(ErrorCode.SAMPLE_ERROR_CODE, null);

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.SAMPLE_ERROR_CODE);
        assertThat(result.getMessage()).isEqualTo(ErrorCode.SAMPLE_ERROR_CODE.getMessage());
    }

    @Test
    void ErrorCode에_메세지를_넣으면_주입한_메세지를_제공한다() throws Exception{
        //given & when
        ApplicationException result = new ApplicationException(ErrorCode.SAMPLE_ERROR_CODE, "샘플에러입니다.");

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.SAMPLE_ERROR_CODE);
        assertThat(result.getMessage()).isEqualTo("샘플에러입니다.");
    }
}
