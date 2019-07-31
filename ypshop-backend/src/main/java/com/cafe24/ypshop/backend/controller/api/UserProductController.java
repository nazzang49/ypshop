package com.cafe24.ypshop.backend.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.service.UserProductService;
import com.cafe24.ypshop.backend.vo.ProductVO;

import io.swagger.annotations.ApiOperation;

//(회원) 상품 컨트롤러
@RequestMapping("/api/product")
@RestController("productAPIController")
public class UserProductController {

	@Autowired
	private UserProductService userProductService;

	//상품 목록
	@ApiOperation(value="상품 목록")
	@GetMapping(value={"/list/{categoryNo}","/list"})
	public ResponseEntity<JSONResult> getList(@ModelAttribute ProductVO productVO) {
		
		List<ProductVO> productList = userProductService.상품목록(productVO);

		if(productList.isEmpty()) {
			JSONResult result = JSONResult.success("상품 목록 없음");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("productList", productList);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//상품 상세 >> 기본 정보, 이미지, 옵션
	@ApiOperation(value="상품 상세")
	@GetMapping(value="/view/{no}")
	public ResponseEntity<JSONResult> view(@ModelAttribute ProductVO productVO) {
		
		//리턴 데이터
		Map<String, Object> data = userProductService.상품상세(productVO);
		
		if((ProductVO)data.get("pvo")==null) {
			JSONResult result = JSONResult.success("상품 없음");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}	
	
}
