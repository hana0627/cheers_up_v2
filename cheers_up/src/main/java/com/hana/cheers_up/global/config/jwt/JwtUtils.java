package com.hana.cheers_up.global.config.jwt;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.global.config.clock.TimeProvider;
import com.hana.cheers_up.global.config.jwt.mac.MacProvider;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final TimeProvider timeProvider;
    private final MacProvider macProvider;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredMs;



    /**
     * 토큰생성
     */
    public String generateToken(String userId, String nickname, String email, RoleType roleType) {
        verifySecretKeyAndExpiry();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("nickname", nickname);
        claims.put("email", email);
        claims.put("roleType", roleType.toString());

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(timeProvider.getCurrentTime()))
                .setExpiration(new Date(timeProvider.getCurrentTime() + expiredMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 만료여부 확인
     * 만료된 토큰 : return ture
     * 만료되지 않은 토큰 : return false
     */
    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date(timeProvider.getCurrentTime()));
    }

    /**
     * token을 통하여 회원 아이디 추출
     */
    public String getUserId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("userId").toString();
    }


    /**
     * 유효하지 않은 토큰인지 검증(토큰이 조작되었는지 여부확인)
     * 유요하지 않은 토큰 : return ture
     * 유효한 토큰 : return false
     */
    public boolean isInvalidated(String token) {
        // 토큰을 (Header, Payload, Signature)으로 분할
        List<String> chunks = List.of(token.split("\\."));
        if (chunks.size() != 3) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN_FORMAT, "토큰 형식이 올바르지 않습니다.");
        }
        // 시그니처 추출
        String signature = chunks.get(2);
        // 헤더+시그니처를 암호화
        String headerAndClaims = chunks.get(0) + "." + chunks.get(1);
        String expectedSignature = generateSignature(headerAndClaims);
        // 헤더+시그너처를 암호화한 값과 토큰의 Signature가 일치하는지 여부 확인
        return !signature.equals(expectedSignature);
    }


    // 헤더와 시그니처를 통하여 Signature 생성
    private String generateSignature(String headerAndClaims) {
        verifySecretKeyAndExpiry();

        try {
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");

            Mac hmacSha256 = macProvider.getMac("HmacSHA256", secretKeySpec);

            byte[] signatureBytes = hmacSha256.doFinal(headerAndClaims.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {

            throw new ApplicationException(ErrorCode.JWT_SIGNATURE_GENERATION_FAILED, "시그니처 생성 중 오류 발생");
        }
    }

    // 토큰 claims 정보 추출
    private Claims extractClaims(String token) {
        verifySecretKeyAndExpiry();

        return Jwts.parser()
                .verifyWith(getKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // 서명키 생성
    private SecretKey getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private void verifySecretKeyAndExpiry() {
        if (secretKey == null || expiredMs == null) {
            throw new ApplicationException(ErrorCode.JWT_SECRET_KEY_NOT_CONFIGURED,"key 혹은 expiredMs가 존재하지 않습니다.");
        }
    }
}
