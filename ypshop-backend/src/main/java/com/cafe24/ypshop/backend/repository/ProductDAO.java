package com.cafe24.ypshop.backend.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.ypshop.backend.vo.CartVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

@Repository
public class ProductDAO {

	@Autowired
	private SqlSession sqlSession;
	
	//(관리자) 상품 추가
	public boolean insert(ProductVO productVO) {
		return sqlSession.insert("product.insert", productVO)==1;
	}
	
	//(관리자) 상품 추가 >> 상품 번호 리턴
	public Long selectMaxProductNo() {
		return sqlSession.selectOne("product.selectMaxProductNo"); 
	}
	
	//(관리자) 진열 순서
	public Long selectMaxAlignNo(ProductVO productVO) {
		return sqlSession.selectOne("product.selectMaxAlignNo", productVO);
	}
	
	//(관리자) 상품 목록
	public List<ProductVO> selectAllByCategoryNo(Long categoryNo, String searchType, String searchKwd) {
		Map<String, Object> map = new HashMap<>();
		map.put("searchType", searchType);
		map.put("searchKwd", searchKwd);
		map.put("categoryNo", categoryNo);
		return sqlSession.selectList("product.selectAllByCategoryNo", map);
	}
	
	//(관리자) 상품 수정
	public boolean update(ProductVO productVO) {
		return sqlSession.update("product.update", productVO)==1;
	}
	
	//(관리자) 상품 삭제
	public boolean delete(ProductVO productVO) {
		return sqlSession.update("product.delete", productVO)==1;
	}
	
	//(관리자) 상품 삭제 >> 동일 카테고리 내 상품 진열번호 -1
	public boolean updateAlignNoWhenDelete(ProductVO productVO) {
		return sqlSession.update("product.updateAlignNoWhenDelete", productVO)==1;
	}
	
	//(고객) 상품 목록
	public List<ProductVO> selectAllByCategoryNoAndAlignUse(ProductVO productVO) {
		return sqlSession.selectList("product.selectAllByCategoryNoAndAlignUse", productVO);
	}
	
	//(고객 및 관리자) 상품 상세
	public ProductVO selectProductDetailByNo(ProductVO productVO) {
		return sqlSession.selectOne("product.selectProductDetailByNo", productVO);
	}
	
}
