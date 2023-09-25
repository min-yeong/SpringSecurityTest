package com.example.springsecuritytest.user.service

import com.example.springsecuritytest.common.authority.JwtTokenProvider
import com.example.springsecuritytest.common.authority.TokenInfo
import com.example.springsecuritytest.common.exception.InvalidInputException
import com.example.springsecuritytest.common.status.ROLE
import com.example.springsecuritytest.user.dto.LoginDto
import com.example.springsecuritytest.user.dto.UserDtoRequest
import com.example.springsecuritytest.user.dto.UserDtoResponse
import com.example.springsecuritytest.user.entity.User
import com.example.springsecuritytest.user.entity.UserRole
import com.example.springsecuritytest.user.repository.UserRepository
import com.example.springsecuritytest.user.repository.UserRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Transactional
@Service
class UserService(
        private val userRepository: UserRepository,
        private val userRoleRepository: UserRoleRepository,
        private val authenticationManagerBuilder: AuthenticationManagerBuilder,
        private val jwtTokenProvider: JwtTokenProvider

) {
    /*
    * 회원가입
    * */
    fun signUp(userDtoRequest: UserDtoRequest): String {
        // ID 중복 검사
        var user: User? = userRepository.findByEmail(userDtoRequest.email)
        if (user != null) {
//            return "이미 등록된 ID 입니다."
            // Exception 으로 발생하여 처리
            throw InvalidInputException("email", "이미 등록된 EMAIL 입니다.")
        }
        // user dto를 entity 함수로 변환
        user = userDtoRequest.toEntity()
        userRepository.save(user)

        // 회원 권한 저장
        val userRole: UserRole = UserRole(null, ROLE.MEMBER, user)
        userRoleRepository.save(userRole)

        return "회원가입이 완료되었습니다."
    }

    /**
     * 로그인 -> 토큰 발행
     */
    fun login(loginDto: LoginDto): TokenInfo {
        // 아이디와 비밀번호로 UsernamePasswordAuthenticationToken 발행
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        // 생성된 토큰을 authenticationManagerBuilder로 전달,  db user 정보와 비교
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        // 이상이 없는 경우 토큰 발행 후 반환
        return jwtTokenProvider.createToken(authentication)
    }


    /**
     * 회원 정보 조회
     */
    fun searchMyInfo(id: Long): UserDtoResponse {
        val user: User = userRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return user.toDto()
    }

    /**
     * 회원 정보 수정
     */
    fun saveMyInfo(userDtoRequest: UserDtoRequest): String {
        val user: User = userDtoRequest.toEntity()
        userRepository.save(user)
        return "수정 완료되었습니다."
    }
}