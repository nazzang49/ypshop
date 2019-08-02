package com.cafe24.ypshop.backend.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.ypshop.backend.vo.CartVO;
import com.cafe24.ypshop.backend.vo.ProductOptionVO;
import com.cafe24.ypshop.backend.vo.ProductVO;

@Repository
public class ProductOptionDAO {

	@Autowired
	private SqlSession sqlSession;
	
	//(관리자) 상품옵션 추가
	public boolean insertProductOption(ProductOptionVO productOptionVO) {
		return sqlSession.insert("productOption.insertProductOption", productOptionVO)==1;
	}
	
	//(관리자) 상품옵션 삭제
	public boolean deleteProductOption(Long no) {
		return sqlSession.delete("productOption.deleteProductOption", no)==1;
	}
	
	//(회원) 상품옵션 목록
	public List<ProductOptionVO> selectAllProductOptionByNo(ProductVO productVO) {
		return sqlSession.selectList("productOption.selectAllProductOptionByNo", productVO);
	}
	
	//재고수량 수정
	public boolean updateRemainAmount(CartVO cartVO) {
		return sqlSession.update("productOption.updateRemainAmount", cartVO)==1;
	}
}
