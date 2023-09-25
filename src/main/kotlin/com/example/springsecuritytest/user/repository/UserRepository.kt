package com.example.springsecuritytest.user.repository

import com.example.springsecuritytest.user.entity.User
import com.example.springsecuritytest.user.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    // email 로 회원정보 조회
    fun findByEmail(email: String): User?
}

// 회원가입 시 권한 정보 저장
interface UserRoleRepository : JpaRepository<UserRole, Long>