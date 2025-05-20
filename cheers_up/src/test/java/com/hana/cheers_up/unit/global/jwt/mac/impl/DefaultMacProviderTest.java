package com.hana.cheers_up.unit.global.jwt.mac.impl;


import com.hana.cheers_up.global.config.jwt.mac.impl.DefaultMacProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultMacProviderTest {


    private DefaultMacProvider macProvider;

    @BeforeEach
    public void setUp() {
        macProvider = new DefaultMacProvider();
    }

    @Test
    void 유효한_알고리즘과_키로_Mac인스턴스를_가져온다() throws Exception{
        //given
        String algorithm = "HmacSHA256";
        byte[] keyBytes = "testSecretKey12345".getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algorithm);

        //when
        Mac result = macProvider.getMac(algorithm, secretKeySpec);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getAlgorithm()).isEqualTo(algorithm);

    }


    @Test
    void 유효하지않은_알고리즘으로_Mac인스턴스_요청시_예외가_발생한다() {
        //given
        String algorithm = "InvalidAlgorithm";
        byte[] keyBytes = "testSecretKey12345".getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algorithm);

        //when && then
        assertThrows(NoSuchAlgorithmException.class, () -> macProvider.getMac(algorithm, secretKeySpec));
    }


    @Test
    void 유효하지않은_키로_Mac인스턴스_요청시_예외가_발생한다() {
        //given
        String algorithm = "HmacSHA256";
        byte[] keyBytes = "testSecretKey12345".getBytes();

        //when && then
        assertThrows(InvalidKeyException.class, () -> macProvider.getMac(algorithm, null));
    }

}