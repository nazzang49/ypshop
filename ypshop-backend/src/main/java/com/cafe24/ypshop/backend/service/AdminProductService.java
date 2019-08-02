package com.cafe24.ypshop.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.dto.ImageDTO;
import com.cafe24.ypshop.backend.repository.ImageDAO;
import com.cafe24.ypshop.backend.repository.OptionDAO;
import com.cafe24.ypshop.backend.repository.ProductDAO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.OptionVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

//(관리자) 상품 서비스
@Service
public class AdminProductService {

	@Autowired
	private ProductDAO productDao;
	
	@Autowired
	private ImageDAO imageDao;
	
	@Autowired
	private OptionDAO optionDao;
	
	//상품 목록 >> 비진열 포함
	public List<ProductVO> 상품목록(Long categoryNo, String searchType, String searchKwd) {
		
		System.out.println("검색 타입 : "+categoryNo);
		
		return productDao.selectAllByCategoryNo(categoryNo, searchType, searchKwd);
	}
	
	//상품 추가
	@Transactional
	public boolean 상품추가(ProductVO productVO) {
		//진열번호 >> 동일 카테고리 기준
		Long alignNo = productDao.selectMaxAlignNo(productVO);
		productVO.setAlignNo(++alignNo);
		productDao.insert(productVO);
		Long productNo = productDao.selectMaxProductNo();
		
		//이미지 추가 >> set 상품번호
		for(ImageVO imageVO : productVO.getImageList()) {
			
			//dto 변환
			ImageDTO imageDTO = new ImageDTO();
			imageDTO.setProductNo(productNo);
			imageDTO.setUrl(imageVO.getUrl());
			imageDTO.setRepOrBasic(imageVO.getRepOrBasic());
			
			imageDao.insert(imageDTO);
		}
		
		//옵션 추가 >> set 상품번호
		for(OptionVO optionVO : productVO.getOptionList()) {
			optionVO.setProductNo(productNo);
			optionDao.insert(optionVO);
		}
		return true;
	}
	
	//상품 수정
	public boolean 상품수정(ProductVO productVO) {
		return productDao.update(productVO);
	}
	
	//상품 삭제 >> 진열번호 -1
	@Transactional
	public boolean 상품삭제(ProductVO productVO) {
		productDao.delete(productVO);
		productDao.updateAlignNoWhenDelete(productVO);
		return true;
	}
	
	//진열 여부 및 번호 수정
	
	
}
