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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.ypshop.backend.dto.ImageDTO;
import com.cafe24.ypshop.backend.dto.JSONResult;
import com.cafe24.ypshop.backend.service.AdminImageService;
import com.cafe24.ypshop.backend.service.AdminOptionService;
import com.cafe24.ypshop.backend.service.AdminProductOptionService;
import com.cafe24.ypshop.backend.service.AdminProductService;
import com.cafe24.ypshop.backend.validator.CustomCollectionValidator;
import com.cafe24.ypshop.backend.vo.OptionVO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.ProductVO;
import io.swagger.annotations.ApiOperation;

//(관리자) 상품 컨트롤러
@RequestMapping("/api/admin/product")
@RestController("adminProductAPIController")
public class AdminProductController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AdminProductService adminProductService;
	
	@Autowired
	private AdminImageService adminImageService;
	
	@Autowired
	private AdminOptionService adminOptionService;
	
	@Autowired
	private AdminProductOptionService adminProductOptionService;
	
	@Autowired CustomCollectionValidator customCollectionValidator;
	
	@ApiOperation(value="상품 목록")
	@GetMapping(value= {"/list", "/list/{categoryNo}"})
	public JSONResult getProductList(@ModelAttribute ProductVO productVO) {
		
		//관리자 인증
	
		List<ProductVO> productList = adminProductService.상품목록(productVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("productList", productList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="상품 추가")
	@PostMapping(value="/add")
	public ResponseEntity<JSONResult> productAdd(@RequestBody @Valid ProductVO productVO,
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
		
		//상품 추가 성공 >> 상품 번호 리턴
		boolean flag = adminProductService.상품추가(productVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="상품 수정")
	@PutMapping(value="/update/{no}")
	public ResponseEntity<JSONResult> productUpdate(@ModelAttribute @Valid ProductVO productVO,
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
		
		boolean flag = adminProductService.상품수정(productVO);
	
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);	
	}
	
	@ApiOperation(value="상품 삭제")
	@DeleteMapping(value="/delete/{no}")
	public ResponseEntity<JSONResult> productDelete(@ModelAttribute ProductVO productVO) {
		
		//관리자 인증
		
		//valid
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ProductVO>> validatorResults = validator.validateProperty(productVO, "categoryNo");
		
		if(!validatorResults.isEmpty()) {
			String msg = "";
			for(ConstraintViolation<ProductVO> validatorResult : validatorResults) {
				if("categoryNo".equals(validatorResult.getPropertyPath().toString())) {
					msg = messageSource.getMessage("NotNull.productVO.categoryNo", null, LocaleContextHolder.getLocale());
					break;
				}
			}
			JSONResult result = JSONResult.fail(msg);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		
		boolean flag = adminProductService.상품삭제(productVO);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
	/* 
	 * 이미지, 옵션 >> 상품번호 기준
	 * 
	 * 목록 >> select >> productNo
	 * 추가 >> insert >> 정보 리스트에 담아 전송 + productNo
	 * 삭제 >> delete >> 번호 리스트에 담아 전송
	 */
	
	
	@ApiOperation(value="이미지 목록")
	@GetMapping(value= "/{productNo}/image/list")
	public JSONResult getImageList(@PathVariable(value="productNo") Long productNo) {
		
		//관리자 인증
	
		List<ImageVO> imageList = adminImageService.이미지목록(productNo);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("imageList", imageList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="이미지 추가")
	@PostMapping(value="/{productNo}/image/add")
	public ResponseEntity<JSONResult> imageAdd(@RequestBody @Valid List<ImageDTO> imageDtoList,
											   BindingResult br,
											   @PathVariable(value="productNo") Long productNo) {

		//관리자 인증
		
		//imageDtoList valid
		customCollectionValidator.validate(imageDtoList, br);
		
		if(br.hasErrors()) {
			List<ObjectError> errorList = br.getAllErrors();
			for(ObjectError error : errorList) {
				String msg = error.getDefaultMessage();
				JSONResult result = JSONResult.fail(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);	
			}
		}
		
		String returnMsg = adminImageService.이미지추가(imageDtoList);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("returnMsg", returnMsg);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="이미지 삭제")
	@DeleteMapping(value="/image/delete")
	public ResponseEntity<JSONResult> imageDelete(@RequestParam(value="no", required=true, defaultValue="0") List<Long> imageNoList) {
		
		//관리자 인증
		
		boolean flag = adminImageService.이미지삭제(imageNoList);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@ApiOperation(value="옵션 목록")
	@GetMapping(value= "/{productNo}/option/list")
	public JSONResult getOptionList(@PathVariable(value="productNo") Long productNo) {
		
		//관리자 인증
	
		List<OptionVO> optionList = adminOptionService.옵션목록(productNo);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("optionList", optionList);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	@ApiOperation(value="옵션 추가")
	@PostMapping(value="/{productNo}/option/add")
	public ResponseEntity<JSONResult> OptionAdd(@RequestParam(value="name", required=true) List<String> optionNameList,
												@RequestParam(value="depth", required=true) List<Long> optionDepthList,
												@PathVariable(value="productNo") Long productNo) {
		
		//관리자 인증
		
		//valid by JS
		
		String returnMsg = adminOptionService.옵션추가(optionNameList, optionDepthList, productNo);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("returnMsg", returnMsg);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//옵션 삭제
	@ApiOperation(value="옵션 삭제")
	@DeleteMapping(value="/option/delete")
	public JSONResult optionDelete(@RequestParam(value="no", required=true, defaultValue="0") List<Long> optionNoList) {
		
		//관리자 인증
		
		boolean flag = adminOptionService.옵션삭제(optionNoList);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
	//상품옵션 추가
	@ApiOperation(value="상품옵션 추가")
	@PostMapping(value="/{productNo}/productOption/add")
	public ResponseEntity<JSONResult> productOptionAdd(@RequestParam(value="firstOptionNo", required=true, defaultValue="0") List<Long> firstOptionNoList,
													   @RequestParam(value="secondOptionNo", required=true, defaultValue="0") List<Long> secondOptionNoList,
													   @RequestParam(value="remainAmount", required=true, defaultValue="0") List<Long> remainAmountList,
											   		   @PathVariable(value="productNo") Long productNo) {

		//관리자 인증
		
		//valid by JS
		
		boolean flag = adminProductOptionService.상품옵션추가(firstOptionNoList, secondOptionNoList, remainAmountList, productNo);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//상품옵션 삭제
	@ApiOperation(value="상품옵션 삭제")
	@DeleteMapping(value="/productOption/delete")
	public JSONResult productOptionDelete(@RequestParam(value="no", required=true, defaultValue="0") List<Long> productOptionNoList) {
		
		//관리자 인증
		
		boolean flag = adminProductOptionService.상품옵션삭제(productOptionNoList);
		
		//리턴 데이터
		Map<String, Object> data = new HashMap<>();
		data.put("flag", flag);
		JSONResult result = JSONResult.success(data);
		return result;
	}
	
}
