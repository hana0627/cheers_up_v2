package com.hana.cheers_up.global.config.jwt.mac.impl;

import com.hana.cheers_up.global.config.jwt.mac.MacProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class DefaultMacProvider implements MacProvider {
    @Override
    public Mac getMac(String algorithm, SecretKeySpec secretKeySpec) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return mac;
    }
}
