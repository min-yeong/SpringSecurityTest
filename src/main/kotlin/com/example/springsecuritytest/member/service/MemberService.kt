package com.example.springsecuritytest.member.service

import com.example.springsecuritytest.common.exception.InvalidInputException
import com.example.springsecuritytest.member.dto.MemberDtoRequest
import com.example.springsecuritytest.member.entity.Member
import com.example.springsecuritytest.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(private val memberRepository: MemberRepository) {
    /*
    * 회원가입
    * */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // ID 중복 검사
        var member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (member != null) {
//            return "이미 등록된 ID 입니다."
            // Exception 으로 발생하여 처리
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }
//        member = Member(
//                null,
//                memberDtoRequest.loginId,
//                memberDtoRequest.password,
//                memberDtoRequest.name,
//                memberDtoRequest.birthDate,
//                memberDtoRequest.gender,
//                memberDtoRequest.email
//        )
        // member dto를 entity 함수로 변환
        member = memberDtoRequest.toEntity()
        memberRepository.save(member)
        return "회원가입이 완료되었습니다."
    }
}