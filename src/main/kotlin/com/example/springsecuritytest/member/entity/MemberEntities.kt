package com.example.springsecuritytest.member.entity

import com.example.springsecuritytest.common.status.Gender
import jakarta.persistence.*
import java.time.LocalDate

// Client로 부터 받은 DTO 정보를 DB에 저장하기 위한 Entity 생성
@Entity
@Table(
        // loginId 중복 불허를 위해 unique column 으로 지정
        uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["loginId"])]
)
class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false, length = 30, updatable = false)
        val loginId: String,

        @Column(nullable = false, length = 100)
        val password: String,

        @Column(nullable = false, length = 10)
        val name: String,

        @Column(nullable = false)
        @Temporal(TemporalType.DATE)     // 날짜형만 입력 가능
        val birthDate: LocalDate,

        @Column(nullable = false, length = 5)
        @Enumerated(EnumType.STRING)     // enum class code insert
        val gender: Gender,

        @Column(nullable = false, length = 30)
        val email: String,

        )