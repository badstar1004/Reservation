package com.store.reservation.config.jwt;

import static com.store.reservation.config.jwt.util.TokenUtil.generateRandomToken;

import com.store.reservation.domain.user.common.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthenticationProvider {

    private final String SECRET_KEY = "ReservationStore202304";
    private final long TOKENVALIDTIME = 1000L * 60 * 60 * 24;   // 하루

    /**
     * 토큰 생성
     *
     * @param userId    회원 아이디
     * @param userEmail 회원 이메일
     * @param userType  회원구분
     * @return String
     */
    public String createToken(Long userId, String userEmail, UserType userType) {
        SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");
        Claims claims = Jwts.claims().setSubject(userEmail).setId(generateRandomToken());
        claims.put("roles", userType);

        Date now = new Date();

        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKENVALIDTIME))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    /**
     * JWT 토큰 검증
     *
     * @param token 토큰
     * @return boolean
     */
    public boolean validateToken(String token) {
        SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }
}
