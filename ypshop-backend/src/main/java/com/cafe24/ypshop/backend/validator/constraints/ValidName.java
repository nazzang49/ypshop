package com.cafe24.ypshop.backend.validator.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cafe24.ypshop.backend.validator.NameValidator;

@Retention(RUNTIME)
@Target(FIELD)
//사용자 정의 validator를 해당 어노테이션이 명시된 곳에 사용하겠다는 선언
@Constraint(validatedBy=NameValidator.class)
public @interface ValidName {

	//입력 실패 시, 리턴 받을 메시지
	String message() default "Invalid Name";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
