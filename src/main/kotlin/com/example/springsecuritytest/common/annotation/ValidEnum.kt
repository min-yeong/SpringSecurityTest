package com.example.springsecuritytest.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValidEnumValidator::class])
// 어노테이션 클래스 생성
annotation class ValidEnum (
    // 유효성 검사에 통과하지 못하는 경우 return message
    val message: String = "Invalid enum value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    // enumClass : enum class로 정의된 class (gender)
    val enumClass: KClass<out Enum<*>>
)

// enum class 유효성 검사 class 생성
class ValidEnumValidator : ConstraintValidator<ValidEnum, Any> {
    private lateinit var enumValues: Array<out Enum<*>>

    override fun initialize(annotation: ValidEnum) {
        enumValues = annotation.enumClass.java.enumConstants
    }
    // value : 입력 받은 파라미터
    // enumValues : gender enum class 로 설정한 value 값 (MAN, WOMAN)
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        // TODO("Not yet implemented")
        if (value == null) {
            return true
        }
        // any : 뒤에 있는 조건이 하나라도 만족이면 true 반환 (유효성 검사 통화)
        return enumValues.any { it.name == value.toString()}
    }
}
