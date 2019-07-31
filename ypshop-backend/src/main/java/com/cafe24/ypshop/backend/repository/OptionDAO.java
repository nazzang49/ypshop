package com.cafe24.ypshop.backend.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cafe24.ypshop.backend.vo.OptionVO;

@Repository
public class OptionDAO {

	@Autowired
	private SqlSession sqlSession;
	
	//(관리자) 옵션 추가
	public boolean insert(OptionVO optionVO) {
		return sqlSession.insert("option.insert", optionVO)==1;
	}
	
	//(관리자) 옵션 삭제
	public boolean delete(Long no) {
		return sqlSession.delete("option.delete", no)==1;
	}
	
	//(관리자) 상품별 옵션 목록
	public List<OptionVO> selectAllOptionByProductNo(Long productNo) {
		return sqlSession.selectList("option.selectAllOptionByProductNo", productNo);
	}
	
	//(관리자) 옵션 중복 체크
	public OptionVO checkExist(Long productNo, String optionName) {
		Map<String, Object> map = new HashMap<>();
		map.put("optionName", optionName);
		map.put("productNo", productNo);
		return sqlSession.selectOne("option.checkExist", map); 
	}
}
