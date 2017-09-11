package com.jwt.springboot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class TokenAuthenticationService {


    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    private static final Logger logger = Logger.getLogger("TokenAuthenticationService");


    public static void addAuthentication(HttpServletResponse response, String user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_name", user);
        claims.put("login_request_attempt_date", new Date());
        String authToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + authToken);
    }

    public static Authentication getAuthencation(HttpServletRequest request) {
        String authToken = request.getHeader(HEADER_STRING);
        if (Objects.nonNull(authToken)) {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(authToken.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = (String) body.get("user_name");
            Date loginRequestAttempDate = new Date((Long) body.get("login_request_attempt_date"));
            logger.info("Login Request Attempt Date : " + loginRequestAttempDate);
            return Objects.nonNull(user) ?
                    new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) : null;
        }

        return null;
    }
}
