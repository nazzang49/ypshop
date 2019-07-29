package com.cafe24.ypshop.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.cafe24.ypshop.backend.dto.JSONResult;

//사용자 정의 예외 처리
@ControllerAdvice
public class GlobalExceptionHandler {

	//무결성 제약조건 >> 500
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<JSONResult> dataIntegrityViolationException() {
		System.out.println("무결성 제약조건 위반");
		JSONResult result = JSONResult.fail("무결성 제약조건 위반");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//PK 중복 >> 500
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<JSONResult> duplicateKeyException() {
		System.out.println("PK 중복 위반");
		JSONResult result = JSONResult.fail("PK 중복 위반");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//메소드 인자 데이터 타입 오류 >> 400
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<JSONResult> illegalArgumentException() {
		System.out.println("Path Variable NotNull 위반");
		JSONResult result = JSONResult.fail("Path Variable NotNull 위반");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	
	//메소드 인자 데이터 타입 오류 >> 400
	@ExceptionHandler(BindException.class)
	public ResponseEntity<JSONResult> bindException() {
		System.out.println("메소드 인자 데이터 타입 오류 - Bind");
		JSONResult result = JSONResult.fail("메소드 인자 데이터 타입 오류 - Bind");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	
	//NULL 입력 불가 >> 400
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<JSONResult> nullPointerException() {
		System.out.println("NULL 입력 불가");
		JSONResult result = JSONResult.fail("NULL 입력 불가");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	
	//SQL 쿼리 구문 오류 >> 500
	@ExceptionHandler(BadSqlGrammarException.class)
	public ResponseEntity<JSONResult> badSqlGrammarException() {
		System.out.println("SQL 쿼리 구문 오류");
		JSONResult result = JSONResult.fail("SQL 쿼리 구문 오류");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//관련 클래스 부재 >> 500
	@ExceptionHandler(ClassNotFoundException.class)
	public ResponseEntity<JSONResult> classNotFoundException() {
		System.out.println("관련 클래스 부재");
		JSONResult result = JSONResult.fail("관련 클래스 부재");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//요청 처리 관련 메소드 상태 부적합 >> 500
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<JSONResult> illegalStateException() {
		System.out.println("요청 처리 관련 메소드 상태 부적합");
		JSONResult result = JSONResult.fail("요청 처리 관련 메소드 상태 부적합");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	//해당 메소드 부재 >> 500
	@ExceptionHandler(NoSuchMethodException.class)
	public ResponseEntity<JSONResult> NnSuchMethodException() {
		System.out.println("해당 메소드 부재");
		JSONResult result = JSONResult.fail("해당 메소드 부재");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
}
