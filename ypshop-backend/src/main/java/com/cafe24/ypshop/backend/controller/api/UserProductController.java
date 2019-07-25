package com.cafe24.ypshop.backend.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
	public JSONResult getList(@ModelAttribute ProductVO productVO) {
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		
		List<ProductVO> productList = userProductService.상품목록(productVO);
		
		//범위 초과
		if(productList.isEmpty()&&productVO.getCategoryNo()!=null) {
			data.put("returnPage", "redirect:/api/product/list");
			JSONResult result = JSONResult.fail("카테고리 번호 범위 초과", data);
			return result;
		}
		
		//상품 없음
		if(productList.isEmpty()) {
			JSONResult result = JSONResult.fail("상품 없음");
			return result;
		}
		
		data.put("productList", productList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	//상품 상세 >> 기본 정보, 이미지, 옵션
	@ApiOperation(value="상품 상세")
	@GetMapping(value="/detail/{no}")
	public JSONResult detail(@ModelAttribute ProductVO productVO) {
		
		Map<String, Object> data = userProductService.상품상세(productVO);
		
		//범위 초과 >> 전체 목록
//		if(productList.isEmpty()&&productVO.getNo()!=null) {
//			data.put("returnPage", "redirect:/api/product/list");
//			JSONResult result = JSONResult.success(data);
//			return result;
//		}
//		
//		//상품 없음 >> 안내 문구
//		if(productList.isEmpty()) {
//			data.put("alert", "None of products is available");
//			JSONResult result = JSONResult.success(data);
//			return result;
//		}
		
		JSONResult result = JSONResult.success(data);
		return result;
	}	
	
}
