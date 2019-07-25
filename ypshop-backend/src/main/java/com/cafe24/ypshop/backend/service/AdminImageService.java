package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.repository.ImageDAO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

//(관리자) 이미지 서비스
@Service
public class AdminImageService {

	@Autowired
	private ImageDAO imageDao;
	
	//이미지 목록
	public List<ImageVO> 이미지목록(Long productNo) {
		return imageDao.selectAllImageByProductNo(productNo);
	}
	
	//이미지 추가 >> 리스트 처리
	public boolean 이미지추가(List<String> imageUrlList, Long productNo) {
		boolean flag = true;
		for(String url : imageUrlList) {
		
			//이미지 경로 작업
			
			//1개 이상 실패 >> fail
			flag = imageDao.insert(new ImageVO(productNo, url, "B"));
		}
		return flag;
	}
	
	//이미지 삭제
	public boolean 이미지삭제(List<Long> imageNoList) {
		boolean flag = true;
		for(Long no : imageNoList) {
			//1개 이상 실패 >> fail
			flag = imageDao.delete(no);
		}
		return flag;
	}
	
	//특정 카테고리 내 상품 썸네일 목록 >> 비진열 포함
	public List<ImageVO> 썸네일(ProductVO productVO) {
		return imageDao.selectAllThumbnailByCategoryNo(productVO);
	}
	
}
