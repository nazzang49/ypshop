package com.cafe24.ypshop.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.repository.ImageDAO;
import com.cafe24.ypshop.backend.repository.ProductDAO;
import com.cafe24.ypshop.backend.repository.ProductOptionDAO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.ProductOptionVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

//(회원) 상품 서비스
@Service
public class UserProductService {
	
	@Autowired
	private ProductDAO productDao;
	
	@Autowired
	private ImageDAO imageDao;
	
	@Autowired
	private ProductOptionDAO productOptionDao;
	
	//상품 목록
	public List<ProductVO> 상품목록(ProductVO productVO) {
		return productDao.selectAllByCategoryNoAndAlignUse(productVO);
	}
	
	//상품 상세 >> 기본 정보, 이미지, 옵션
	@Transactional
	public Map<String, Object> 상품상세(ProductVO prodcutVO) {
		//기본 정보
		ProductVO pvo = productDao.selectProductDetailByNo(prodcutVO);
		//상품별 이미지
		List<ImageVO> imageList = imageDao.selectAllImageByProductNo(prodcutVO.getNo());
		//상품별 옵션
		List<ProductOptionVO> productOptionList = productOptionDao.selectAllProductOptionByNo(prodcutVO);
		
		Map<String, Object> map = new HashMap<>();
		map.put("pvo", pvo);
		map.put("imageList", imageList);
		map.put("productOptionList", productOptionList);
		
		return map;
	}
	
}
