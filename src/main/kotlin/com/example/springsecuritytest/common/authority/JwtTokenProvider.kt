package com.example.springsecuritytest.common.authority

import com.example.springsecuritytest.common.dto.CustomUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
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
                .claim("userId", (authentication.principal as CustomUser).userId) // userId 값을 추가하여 사용자 토큰 커스텀
                .setIssuedAt(now) // 현재 발행시간
                .setExpiration(accessExpiration) // 만료기간
                .signWith(key, SignatureAlgorithm.HS256) // 키 사용 알고리즘
                .compact()

        // Bearer 에 위에서 생성한 accessToken 을 담아서 TokenInfo 로 반환
        return TokenInfo("Bearer", accessToken)
    }

    /**
     * Token 추출
     */
    fun getAuthentication(token: String): Authentication {
        // getClaims fun 에서 token으로 claims 조회
        val claims: Claims = getClaims(token)

        // claims auth가 존재하지 않으면 잘못된 토큰 (토큰 생성 부분에서 claims에 auth를 추가하는 부분이 있음)
        val auth = claims["auth"] ?: throw RuntimeException("잘몬된 토큰입니다.")
        val userId = claims["userId"] ?: throw RuntimeException("잘몬된 토큰입니다.")

        // 권한 정보 추출
        val authorities: Collection<GrantedAuthority> = (auth as String)
                .split(",")
                .map {SimpleGrantedAuthority(it)}

        val principal: UserDetails = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)
        // 조회한 권한 정보로 UsernamePasswordAuthenticationToken 생성
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    /**
     * Token 검증
     */
    fun validateToken(token: String): Boolean {
        try {
            // Token으로 Claims 정보 조회
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {} // Invalid JWT Token
                is MalformedJwtException -> {} // Invalid JWT Token
                is ExpiredJwtException -> {} // Expired JWT Token
                is UnsupportedJwtException -> {} // Unsupported JWT Token
                is IllegalArgumentException -> {} // JWT claims string is empty
                else -> {} // else
            }
            println(e.message)
        }
        return false
    }

    // claims 추출
    private fun getClaims(token: String): Claims =
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
}