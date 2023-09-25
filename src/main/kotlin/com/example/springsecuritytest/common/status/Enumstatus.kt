package com.example.springsecuritytest.common.status

// enum type Gender
enum class Gender(val desc: String ) {
    MAN("남"),
    WOMAN("여")
}

// enum type UserType
enum class UserType(val desc: String ) {
    ADMIN("관리자"),
    MEMBER("회원")
}

// return result code 생성
enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다.")
}

// 회원 권한
enum class ROLE {
    MEMBER
}