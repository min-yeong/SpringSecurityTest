package com.example.springsecuritytest.user.dto

import com.example.springsecuritytest.common.annotation.ValidEnum
import com.example.springsecuritytest.common.status.UserType
import com.example.springsecuritytest.user.entity.User
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//data class MemberDtoRequest (
//        val id: Long?,
//        val loginId: String,
//        val password: String,
//        val name: String,
//        val birthDate: LocalDate,
//        val gender: Gender,
//        val email: String,
//)

// validation check를 위해 모두 string (null 허용 변수로 변경)
data class UserDtoRequest (
        var id: Long?,
        // dto 로 받는 값들에 validation 추가
        // 빈값 입력 불가
        @field:NotBlank
        @field:Email
        // email 와 _email 연결
        @JsonProperty("email")
        private val _email: String?,

        @field:NotBlank
        // 입력 패턴 정규식
        @field:Pattern(
                regexp="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
                message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
        )
        @JsonProperty("password")
        private val _password: String?,

        @field:NotBlank
        @JsonProperty("name")
        private val _name: String?,

        @field:NotBlank
        // ValidEnum으로 생성한 annotation 호출
        @field:ValidEnum(enumClass = UserType::class,
                message = "ADMIN 이나 MEMBER 중 하나를 선택해주세요")
        @JsonProperty("userType")
        private val _userType: String?,

        @JsonProperty("point")
        private val _point: Long?,
) {
    val email: String
        // _loginId 는 null 값도 허용 되는 타입이지만 loginId는 null을 허용하지 않는 변수로 선언했기 때문에 !! 를 붙여줘야함
        // !! : null 값이 들어갈 수있는 변수 이지만 null 값이 아님을 보장
        get() = _email!!
    val password: String
        get() = _password!!
    val name: String
        get() = _name!!
    // UserType enum class
    val userType: UserType
        get() = UserType.valueOf(_userType!!)
    val point: Long
        get() = _point!!

    // string 에 .toLocalDate()를 입력하면 LocalDate로 반환하는 확장 함수 생성
    private fun String.toLocalDate(): LocalDate =
            // LocalDate의 format 형식 추가
            LocalDate.parse(this, DateTimeFormatter.ofPattern("yyy-MM-dd"))

    // entity로 변환해서 반환하는 함수 생성
    fun toEntity(): User =
            User(id, email, password, name, userType, point)
}


// 로그인 DTO
data class LoginDto(
        @field:NotBlank
        @JsonProperty("email")
        private val _email: String?,

        @field:NotBlank
        @JsonProperty("password")
        private val _password: String?,
) {
    val email: String
        get() = _email!!
    val password: String
        get() = _password!!
}

data class UserDtoResponse (
        val id: Long,
        val email: String,
        val name: String,
        val userType: String,
        val point: Long,
)