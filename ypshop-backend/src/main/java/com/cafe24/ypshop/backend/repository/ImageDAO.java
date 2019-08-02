package com.cafe24.ypshop.backend.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.ypshop.backend.dto.ImageDTO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

@Repository
public class ImageDAO {

	@Autowired
	private SqlSession sqlSession;
	
	//(관리자) 이미지 중복 체크
	public ImageVO checkExist(ImageDTO imageDTO) {
		return sqlSession.selectOne("image.checkExist", imageDTO);
	}
	
	//(관리자) 이미지 추가
	public boolean insert(ImageDTO imageDTO) {
		return sqlSession.insert("image.insert", imageDTO)==1;
	}
	
	//(관리자) 이미지 삭제
	public boolean delete(Long no) {
		return sqlSession.delete("image.delete", no)==1;
	}
	
	//(관리자) 상품별 이미지 목록
	public List<ImageVO> selectAllImageByProductNo(Long productNo) {
		return sqlSession.selectList("image.selectAllImageByProductNo", productNo);
	}
	
	//(회원) 카테고리별 상품 썸네일 목록
	public List<ImageVO> selectAllThumbnailByCategoryNo(ProductVO productVO) {
		return sqlSession.selectList("image.selectAllThumbnailByCategoryNo", productVO);
	}
	
}
