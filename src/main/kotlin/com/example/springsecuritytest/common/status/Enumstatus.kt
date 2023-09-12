package com.example.springsecuritytest.common.status

// enum type 으로 코드값 제한
enum class Gender(val desc: String ) {
    MAN("남"),
    WOMAN("여")
}

// return result code 생성
enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다.")
}