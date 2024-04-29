package kusitms.duduk.domain.security.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import kusitms.duduk.domain.security.jwt.JwtTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.access.header}")
    private String accessTokenHeader;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;

    // 만약에 SecretKey가 Encoding 안돼 있다면 객체 생성과 동시에 Encoding 합니다.

    /**
     * @PostConstruct protected void init() { secretKey =
     * Base64.getEncoder().encodeToString(secretKey.getBytes()); }
     **/

    public String createAccessToken(String email) {
        return createToken(email, accessTokenExpiration);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenExpiration);
    }

    private String createToken(final String subject, final long expiration) {
        long now = new Date().getTime();

        return Jwts.builder()
            // 토큰의 발급 시간을 기록
            .setIssuedAt(new Date(now))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .setExpiration(new Date(now + expiration))
            // 토큰의 주제, 사용자를 설정
            .setSubject(subject)
            // 토큰을 발급한 주체를 설정
            .setIssuer(issuer)
            // 토큰이 JWT 타입임을 명시
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .compact();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessTokenHeader))
            .filter(refreshToken -> StringUtils.hasText(refreshToken))
            .filter(refreshToken -> refreshToken.startsWith(BEARER_PREFIX))
            .map(refreshToken -> refreshToken.substring(7));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshTokenHeader))
            .filter(refreshToken -> StringUtils.hasText(refreshToken))
            .filter(refreshToken -> refreshToken.startsWith(BEARER_PREFIX))
            .map(refreshToken -> refreshToken.substring(7));
    }

    public Optional<String> extractOAuthToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("OAuth"))
            .filter(oAuthToken -> StringUtils.hasText(oAuthToken))
            .map(oAuthToken -> oAuthToken.toLowerCase(Locale.ROOT));
    }

    public boolean isTokenValid(final String token) {
        try {
            // Token Body에 저장된 만료 시간을 기준으로 토큰의 유효성을 판단합니다.
            return !parseClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Claims는 토큰에 포함된 정보의 조각입니다.
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
	.setSigningKey(secretKey)
	.build()
	.parseClaimsJws(token)
	.getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,
        String reIssuedRefreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessTokenHeader, accessToken);
        response.setHeader(refreshTokenHeader, reIssuedRefreshToken);
    }
    // Subject는 토큰의 주체를 나타내는 중요한 정보로 사용자의 id나 email을 저장합니다.

    public Optional<String> getSubject(String accessToken) {
        return Optional.ofNullable(parseClaims(accessToken).getSubject());
    }

    public JwtTokenInfo createTokenInfo(String email) {
        return JwtTokenInfo.builder()
            .accessToken(createAccessToken(email))
            .refreshToken(createRefreshToken(email))
            .build();
    }
}
