package com.cafe24.ypshop.backend.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.service.MemberService;
import com.cafe24.ypshop.backend.vo.MemberVO;
import io.swagger.annotations.ApiOperation;

//회원
@RequestMapping("/api/member")
@RestController("memberAPIController")
public class MemberController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MemberService memberService;
	
	@ApiOperation(value="조인")
	@PostMapping(value="/join")
	public ResponseEntity<JSONResult> join(@ModelAttribute @Valid MemberVO memberVO,
						   				   BindingResult br) {
		
		//valid
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
		
		boolean flag = memberService.조인(memberVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="아이디 중복 체크")
	@PostMapping(value="/checkid")
	public JSONResult checkid(@ModelAttribute MemberVO memberVO) {
		
		boolean flag = memberService.아이디중복체크(memberVO);
				
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="로그인")
	@PostMapping(value="/login")
	public ResponseEntity<JSONResult> login(@ModelAttribute MemberVO memberVO) {
		
		//valid
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<MemberVO>> validatorResults = validator.validateProperty(memberVO, "id");
		validatorResults.addAll(validator.validateProperty(memberVO, "password"));
		if(!validatorResults.isEmpty()) {
			String msg = "";
			for(ConstraintViolation<MemberVO> validatorResult : validatorResults) {
				if("id".equals(validatorResult.getPropertyPath().toString())) {
					msg = messageSource.getMessage("NotEmpty.memberVO.id", null, LocaleContextHolder.getLocale());
					break;
				}else if("password".equals(validatorResult.getPropertyPath().toString())) {
					msg = messageSource.getMessage("NotEmpty.memberVO.password", null, LocaleContextHolder.getLocale());
					break;
				}
			}
			JSONResult result = JSONResult.fail(msg);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		
		boolean flag = memberService.로그인(memberVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="회원조회")
	@GetMapping(value="/{id}")
	public JSONResult get(@ModelAttribute MemberVO memberVO) {
		
		//본인 인증
		
		memberVO = memberService.회원조회(memberVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("memberVO", memberVO);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="회원수정")
	@PutMapping(value="/update")
	public ResponseEntity<JSONResult> update(@ModelAttribute @Valid MemberVO memberVO,
											 BindingResult br) {
		
		//본인 인증
		
		//valid
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
		
		//리턴 데이터
		boolean flag = memberService.회원수정(memberVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="회원탈퇴")
	@DeleteMapping(value="/delete/{id}")
	public JSONResult delete(@ModelAttribute MemberVO memberVO) {
		
		//비밀번호 valid
		
		//본인 인증
		
		boolean flag = memberService.회원탈퇴(memberVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
}
