package com.cafe24.ypshop.backend.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.service.UserCartService;
import com.cafe24.ypshop.backend.service.UserOrderService;
import com.cafe24.ypshop.backend.vo.CartVO;
import com.cafe24.ypshop.backend.vo.OrderDetailVO;
import com.cafe24.ypshop.backend.vo.OrderVO;

import io.swagger.annotations.ApiOperation;

//(회원) 주문 컨트롤러
@RequestMapping("/api/order")
@RestController("orderAPIController")
public class UserOrderController {

	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private UserCartService userCartService;
	
	@ApiOperation(value="장바구니 추가")
	@PostMapping(value="/cart/add")
	public ResponseEntity<JSONResult> cartAdd(@ModelAttribute @Valid CartVO cartVO,
											  BindingResult br) {
		
		//본인 인증
		
		//valid >> 수량 min, max 추가
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
		
		boolean flag = userCartService.장바구니추가(cartVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="장바구니 목록")
	@GetMapping(value="/cart/{memberId}")
	public JSONResult getCartList(@ModelAttribute CartVO cartVO) {

		//본인 인증
		
				
		List<CartVO> cartList = userCartService.장바구니목록(cartVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("cartList", cartList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="장바구니 수량, 상품옵션 수정")
	@PutMapping(value="/cart/update/{no}")
	public ResponseEntity<JSONResult> cartUpdate(@ModelAttribute @Valid CartVO cartVO,
								 BindingResult br) {
		
		//본인 인증
		
		//valid >> 수량 min, max 추가
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
		
		boolean flag = userCartService.장바구니수정(cartVO);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="장바구니 삭제")
	@DeleteMapping(value="/cart/delete")
	public JSONResult cartDelete(@RequestParam(value="no", required=true, defaultValue="0") List<Long> cartNoList) {
		
		//본인 인증
		
		boolean flag = userCartService.장바구니삭제(cartNoList);
		
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="주문 추가")
	@PostMapping(value="/add")
	public ResponseEntity<JSONResult> orderAdd(@ModelAttribute @Valid OrderVO orderVO,
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
		
		String returnMsg = userOrderService.주문추가(orderVO);
		
		System.out.println("리턴 메시지 : "+returnMsg);
		
		Map<String, Object> data = new HashMap<>();
		data.put("returnMsg", returnMsg);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//주문 상세 목록 >> 주문 + 주문 상세 정보
	@ApiOperation(value="주문 상세 목록")
	@GetMapping(value="/list/{memberId}")
	public JSONResult getOrderDetailList(@ModelAttribute OrderVO orderVO) {

		//본인 인증
		
		Map<String, Object> data = userOrderService.주문상세목록(orderVO);
		
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	//주문 취소 >> only 주문 확인, 입금 확인
	
	
}
