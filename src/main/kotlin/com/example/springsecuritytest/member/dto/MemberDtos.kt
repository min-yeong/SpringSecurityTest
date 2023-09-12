package com.example.springsecuritytest.member.dto

import com.example.springsecuritytest.common.annotation.ValidEnum
import com.example.springsecuritytest.common.status.Gender
import com.example.springsecuritytest.member.entity.Member
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
data class MemberDtoRequest (
        val id: Long?,
        // dto 로 받는 값들에 validation 추가
        // 빈값 입력 불가
        @field:NotBlank
        // loginId 와 _loginId 연결
        @JsonProperty("loginId")
        private val _loginId: String?,

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
        @field:Pattern(
                regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
                message = "날짜형식(YYYY-MM-DD)을 확인해주세요"
        )
        @JsonProperty("birthDate")
        private val _birthDate: String?,

        @field:NotBlank
        // ValidEnum으로 생성한 annotation 호출
        @field:ValidEnum(enumClass = Gender::class,
                message = "MAN 이나 WOMAN 중 하나를 선택해주세요")
        @JsonProperty("gender")
        private val _gender: String?,

        @field:NotBlank
        @field:Email
        @JsonProperty("email")
        private val _email: String?
) {
    val loginId: String
        // _loginId 는 null 값도 허용 되는 타입이지만 loginId는 null을 허용하지 않는 변수로 선언했기 때문에 !! 를 붙여줘야함
        // !! : null 값이 들어갈 수있는 변수 이지만 null 값이 아님을 보장
        get() = _loginId!!
    val password: String
        get() = _password!!
    val name: String
        get() = _name!!
    val birthDate: LocalDate
        get() = _birthDate!!.toLocalDate()
    // gender enum class
    val gender: Gender
        get() = Gender.valueOf(_gender!!)
    val email: String
        get() = _email!!

    // string 에 .toLocalDate()를 입력하면 LocalDate로 반환하는 확장 함수 생성
    private fun String.toLocalDate(): LocalDate =
            // LocalDate의 format 형식 추가
            LocalDate.parse(this, DateTimeFormatter.ofPattern("yyy-MM-dd"))

    // entity로 변환해서 반환하는 함수 생성
    fun toEntity(): Member =
            Member(id, loginId, password, name, birthDate, gender, email)
}