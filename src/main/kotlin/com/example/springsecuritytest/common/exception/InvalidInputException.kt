package com.example.springsecuritytest.common.exception

// RuntimeException을 상속받는 Exception 생성
class InvalidInputException (
        val fieldName: String = "",
        message: String = "Invalid Input"
) : RuntimeException(message)