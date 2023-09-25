package com.example.springsecuritytest.common.service

import com.example.springsecuritytest.common.dto.CustomUser
import com.example.springsecuritytest.user.entity.User
import com.example.springsecuritytest.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
) : UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails =
            userRepository.findByEmail(username)
                // 존재하지 않으면 exception 발생
                ?.let {createUserDetails(it) } ?: throw UsernameNotFoundException("해당 유저는 없습니다.")

    // user 인스턴스를 userdetail로 반환
    private fun createUserDetails(user: User): UserDetails =
            CustomUser(
                user.id!!,
                user.email,
                passwordEncoder.encode(user.password),
                user.userRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
            )
}