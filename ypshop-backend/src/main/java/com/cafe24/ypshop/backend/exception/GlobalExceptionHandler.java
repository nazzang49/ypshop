package com.cafe24.ypshop.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.cafe24.ypshop.backend.dto.JSONResult;

//사용자 정의 예외 처리
@ControllerAdvice
public class GlobalExceptionHandler {

	//무결성 제약조건 >> 500
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<JSONResult> dataIntegrityViolationException() {
		JSONResult result = JSONResult.fail("무결성 제약조건 위반");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//PK 중복 >> 500
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<JSONResult> duplicateKeyException() {
		JSONResult result = JSONResult.fail("PK 중복 위반");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//메소드 인자 데이터 타입 오류 >> 400
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<JSONResult> illegalArgumentException() {
		JSONResult result = JSONResult.fail("메소드 인자 데이터 타입 오류");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
}
