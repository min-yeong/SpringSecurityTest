package com.example.springsecuritytest.member.controller

import com.example.springsecuritytest.common.dto.BaseResponse
import com.example.springsecuritytest.member.dto.MemberDtoRequest
import com.example.springsecuritytest.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/member")
@RestController
class MemberController(private val memberService: MemberService) {
    /*
    * 회원가입
    * */
    @PostMapping("/signup")
    // dto 유효성 검사 추가
    // exception 반환을 위해 string -> baseresponse로 return
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }
}