package com.example.springsecuritytest.user

import com.example.springsecuritytest.common.authority.TokenInfo
import com.example.springsecuritytest.common.dto.BaseResponse
import com.example.springsecuritytest.common.dto.CustomUser
import com.example.springsecuritytest.user.dto.LoginDto
import com.example.springsecuritytest.user.dto.UserDtoRequest
import com.example.springsecuritytest.user.dto.UserDtoResponse
import com.example.springsecuritytest.user.service.UserService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
@RestController
class UserController(private val userService: UserService) {
    /*
    * 회원가입
    * */
    @PostMapping("/signup")
    // dto 유효성 검사 추가
    // exception 반환을 위해 string -> baseresponse로 return
    fun signUp(@RequestBody @Valid userDtoRequest: UserDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = userService.signUp(userDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /*
    * 로그인
    * */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = userService.login(loginDto)
        // 토큰 반환
        return BaseResponse(data = tokenInfo)
    }

    /*
    * 회원 정보 보기
    * */
    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<UserDtoResponse> {
        // email를 url에서 아닌 SecurityContextHolder 에서 조회
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = userService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    /*
    * 회원 정보 수정
    * */
    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid userDtoRequest: UserDtoRequest): BaseResponse<UserDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        userDtoRequest.id = userId
        val resultMsg: String = userService.saveMyInfo(userDtoRequest)
        return BaseResponse(message = resultMsg)
    }
}