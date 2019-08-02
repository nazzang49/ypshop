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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.service.AdminOrderService;
import com.cafe24.ypshop.backend.vo.CategoryVO;
import com.cafe24.ypshop.backend.vo.OrderVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//(관리자) 주문 컨트롤러
@RequestMapping("/api/admin/order")
@RestController("adminOrderAPIController")
public class AdminOrderController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AdminOrderService adminOrderService;
	
	@ApiOperation(value="주문 목록")
	@GetMapping(value="/list")
	public JSONResult getList(@RequestParam(value="searchType", required=true, defaultValue="") String searchType,
			  				  @RequestParam(value="searchKwd", required=true, defaultValue="") String searchKwd) {
		
		//관리자 인증
		
		List<OrderVO> orderList = adminOrderService.주문목록(searchType, searchKwd);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("orderList", orderList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="주문 상태 수정")
	@PutMapping(value="/update/{no}")
	public ResponseEntity<JSONResult> udpate(@ModelAttribute OrderVO orderVO) {
		
		//관리자 인증
				
		//valid
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<OrderVO>> validatorResults = validator.validateProperty(orderVO, "status");
		
		if(!validatorResults.isEmpty()) {
			for(ConstraintViolation<OrderVO> validatorResult : validatorResults) {
				String msg = messageSource.getMessage("NotEmpty.orderVO.status", null, LocaleContextHolder.getLocale());
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}
		
		
		boolean flag = adminOrderService.주문상태수정(orderVO);
	
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
}
