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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.security.AuthUser;
import com.cafe24.ypshop.backend.security.SecurityUser;
import com.cafe24.ypshop.backend.service.AdminCategoryService;
import com.cafe24.ypshop.backend.vo.CategoryVO;
import com.google.common.base.Optional;

import io.swagger.annotations.ApiOperation;

//(관리자) 카테고리 컨트롤러
@RequestMapping("/api/admin/category")
@RestController("adminCategoryAPIController")
public class AdminCategoryController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AdminCategoryService adminCategoryService;
	
	@ApiOperation(value="카테고리 목록")
	@GetMapping(value="/list")
	public JSONResult getList(@RequestParam(value="searchType", required=true, defaultValue="") String searchType,
							  @RequestParam(value="searchKwd", required=true, defaultValue="") String searchKwd) {
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		
		//관리자 인증
		
		List<CategoryVO> categoryList = adminCategoryService.카테고리목록(searchType, searchKwd);
		
		data.put("categoryList", categoryList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="카테고리 중복 체크")
	@GetMapping(value="/checkexist")
	public JSONResult checkExist(@ModelAttribute CategoryVO categoryVO) {
		
		//관리자 인증
		
		//valid
		
		boolean flag = adminCategoryService.카테고리중복체크(categoryVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="카테고리 추가")
	@PostMapping(value="/add")
	public ResponseEntity<JSONResult> add(@ModelAttribute @Valid CategoryVO categoryVO,
						  				  BindingResult br) {
		
		//관리자 인증
		
		//valid
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
			
		boolean flag = adminCategoryService.카테고리추가(categoryVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="카테고리 수정")
	@PutMapping(value="/update/{no}")
	public ResponseEntity<JSONResult> udpate(@ModelAttribute CategoryVO categoryVO) {
		
		//관리자 인증
		
		//valid
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<CategoryVO>> validatorResults = validator.validateProperty(categoryVO, "name");
		
		if(!validatorResults.isEmpty()) {
			for(ConstraintViolation<CategoryVO> validatorResult : validatorResults) {
				String msg = messageSource.getMessage("NotEmpty.categoryVO.name", null, LocaleContextHolder.getLocale());
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}
		
		
		boolean flag = adminCategoryService.카테고리수정(categoryVO);
	
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//하위 카테고리 삭제 필요 -ing
	@ApiOperation(value="카테고리 삭제")
	@DeleteMapping(value="/delete/{no}")
	public ResponseEntity<JSONResult> delete(@PathVariable(value="no") Optional<Long> no) {
		
		//관리자 인증
		
		//fail >> no is null
		if(!no.isPresent()) {
			JSONResult result = JSONResult.fail("카테고리 번호 미입력");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		
		boolean flag = adminCategoryService.카테고리삭제(no.get());
		
		//fail >> delete error
		if(!flag) {
			JSONResult result = JSONResult.fail("카테고리 삭제 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
}
