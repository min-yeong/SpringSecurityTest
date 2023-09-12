package com.example.springsecuritytest.common.authority

data class TokenInfo (
        val grantType: String, // jwt 인증 권한 인증 타입
        val accessToken: String, // 실제 인증할 token
)