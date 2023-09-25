package com.example.springsecuritytest.user.entity

import com.example.springsecuritytest.common.entity.BaseEntity
import com.example.springsecuritytest.common.status.ROLE
import com.example.springsecuritytest.common.status.UserType
import com.example.springsecuritytest.user.dto.UserDtoResponse
import jakarta.persistence.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Client로 부터 받은 DTO 정보를 DB에 저장하기 위한 Entity 생성
@Entity
@Table(
        // email 중복 불허를 위해 unique column 으로 지정
        uniqueConstraints = [UniqueConstraint(name = "uk_user_email", columnNames = ["email"])]
)
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false, length = 30, updatable = false)
        val email: String,

        @Column(nullable = false, length = 100)
        val password: String,

        @Column(nullable = false, length = 10)
        val name: String,

        @Column(nullable = false, length = 5)
        @Enumerated(EnumType.STRING)     // enum class code insert
        val userType: UserType,

        @Column(nullable = false)
        val point: Long,

): BaseEntity() {
        // 회원 권한 저장
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
        val userRole: List<UserRole>? = null

        // LocalDate 타입 string 변환 확장 함수
        private fun LocalDate.formatDate(): String =
                this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        // 회원 정보 변경
        // birthDate 은 LocalDate 타입으로 string 으로 변경 포멧 함수 필요
        fun toDto(): UserDtoResponse =
                UserDtoResponse(id!!, email, name, userType.desc, point)
}

// enum class로 생성한 회원 권한 Entity
@Entity
class UserRole(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false, length = 30)
        @Enumerated(EnumType.STRING)
        val role: ROLE,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "fk_user_role_user_id"))
        val user: User
)
