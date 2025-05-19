package com.hana.cheers_up.global.config.jwt.mac;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public interface MacProvider {
    Mac getMac(String algorithm, SecretKeySpec secretKeySpec) throws Exception;
}
