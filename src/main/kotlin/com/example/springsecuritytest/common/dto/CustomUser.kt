package com.example.springsecuritytest.common.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

// Token에 userId를 저장하여 본인 외에 다른 아이디 접속 방지
class CustomUser(
        val userId: Long,
        userName: String,
        password: String,
        authorities: Collection<GrantedAuthority>
) : User(userName, password, authorities)