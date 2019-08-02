package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.dto.ImageDTO;
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
	
	//이미지 추가
	public String 이미지추가(List<ImageDTO> imageDtoList) {
		StringBuilder returnMsg = new StringBuilder();
		for(int i=0;i<imageDtoList.size();i++) {
			ImageDTO imageDTO = imageDtoList.get(i);
			//중복 X >> 추가 성공 >> 성공 메시지
			if(imageDao.checkExist(imageDTO)==null) imageDao.insert(imageDTO);
			//중복 O >> 추가 실패 >> 실패 메시지
			else {
				returnMsg.append((i+1)+"번 ");
				continue;
			}
		}
		if(returnMsg.toString().contains("번")) {
			return returnMsg.append("이미지 추가 실패 >> 중복").toString();
		}
		return returnMsg.append("이미지 추가 성공").toString();
	}
	
	//이미지 삭제
	@Transactional
	public boolean 이미지삭제(List<Long> imageNoList) {
		for(Long no : imageNoList) imageDao.delete(no);
		return true;
	}
	
	//특정 카테고리 내 상품 썸네일 목록 >> 비진열 포함
	public List<ImageVO> 썸네일(ProductVO productVO) {
		return imageDao.selectAllThumbnailByCategoryNo(productVO);
	}
	
}
