package com.example.springsecuritytest.common.authority

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.Date

//
const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30

// 토큰을 생성하고 토큰 정보를 추출
@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy {Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))}

    /**
     * Token 생성
     */
    fun createToken (authentication: Authentication): TokenInfo {
        // authentication 안에 권한 리스트를 , 로 나누어 string으로 저장
        val authorities: String = authentication
                .authorities
                .joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

        // Access Token
        val accessToken = Jwts
                .builder()
                .setSubject(authentication.name)
                .claim("auth", authorities) // string 으로 저장한 권한을 auth에 저장
                .setIssuedAt(now) // 현재 발행시간
                .setExpiration(accessExpiration) // 만료기간
                .signWith(key, SignatureAlgorithm.HS256) // 키 사용 알고리즘
                .compact()

        // Bearer 에 위에서 생성한 accessToken 을 담아서 TokenInfo 로 반환
        return TokenInfo("Bearer", accessToken)
    }

    // 토근 정보 추출
    private fun getClaims(token: String): Claims =
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
}