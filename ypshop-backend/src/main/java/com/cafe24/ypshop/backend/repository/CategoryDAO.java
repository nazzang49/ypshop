package com.cafe24.ypshop.backend.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cafe24.ypshop.backend.vo.CategoryVO;

@Repository
public class CategoryDAO {

	@Autowired
	private SqlSession sqlSession;
		
	//카테고리 추가
	public boolean insert(CategoryVO categoryVO) {
		categoryVO.setNo(1L);
		return sqlSession.insert("category.insert", categoryVO)==1;
	}
	
	//카테고리 목록
	public List<CategoryVO> selectAll(String searchType, String searchKwd) {
		Map<String, String> map = new HashMap<>();
		map.put("searchType", searchType);
		map.put("searchKwd", searchKwd);
		return sqlSession.selectList("category.selectAll", map);
	}
		
	//카테고리 수정
	public boolean update(CategoryVO categoryVO) {
		return sqlSession.update("category.update", categoryVO)==1;
	}
	
	//카테고리 삭제
	public boolean delete(Long no) {
		return sqlSession.delete("category.delete", no)==1;
	}
	
	//카테고리 중복 체크
	public CategoryVO checkExist(CategoryVO categoryVO) {
		return sqlSession.selectOne("category.checkExist", categoryVO);
	}

}