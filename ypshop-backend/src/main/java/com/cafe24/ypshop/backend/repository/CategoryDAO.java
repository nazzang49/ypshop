package com.cafe24.ypshop.backend.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cafe24.ypshop.backend.vo.CategoryVO;

@Repository
public class CategoryDAO {

	@Autowired
	private SqlSession sqlSession;
	
	private final String keyValue = "shop-keyValue";
	
	//카테고리 추가
	public boolean insert(CategoryVO categoryVO) {
		categoryVO.setNo(1L);
		return sqlSession.insert("category.insert", categoryVO)==1;
	}
	
	//카테고리 목록
	public List<CategoryVO> selectAll() {
		return sqlSession.selectList("category.selectAll");
	}
		
	//카테고리 수정
	public boolean update(CategoryVO categoryVO) {
		return sqlSession.update("category.update", categoryVO)==1;
	}
	
	//카테고리 삭제
	public boolean delete(Long no) {
		return sqlSession.delete("category.delete", no)==1;
	}

}