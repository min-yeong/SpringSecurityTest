package com.example.springsecuritytest.member.repository

import com.example.springsecuritytest.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    // LoginId로 회원정보 조회
    fun findByLoginId(loginId: String): Member?
}