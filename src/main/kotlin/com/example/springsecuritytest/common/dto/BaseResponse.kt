package com.example.springsecuritytest.common.dto

import com.example.springsecuritytest.common.status.ResultCode

data class BaseResponse<T> (
        val resultCode: String = ResultCode.SUCCESS.name,
        val date: T? = null,
        val message: String = ResultCode.SUCCESS.msg,
)