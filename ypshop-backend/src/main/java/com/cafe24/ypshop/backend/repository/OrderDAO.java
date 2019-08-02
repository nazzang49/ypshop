package com.cafe24.ypshop.backend.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cafe24.ypshop.backend.vo.OptionVO;
import com.cafe24.ypshop.backend.vo.OrderDetailVO;
import com.cafe24.ypshop.backend.vo.OrderVO;

@Repository
public class OrderDAO {

	@Autowired
	private SqlSession sqlSession;
	
	private final String keyValue="shop-order";
	
	//(회원) 주문 추가
	public boolean insert(OrderVO orderVO) {
		orderVO.setKeyValue(keyValue);
		return sqlSession.insert("order.insert", orderVO)==1;
	}
	
	//(회원) 주문 상세 추가
	public boolean insertOrderDetail(OrderDetailVO orderDetailVO) {
		return sqlSession.insert("order.insertOrderDetail", orderDetailVO)==1;
	}
	
	//(회원) 주문 번호
	public Long selectMaxOrderNo() {
		return sqlSession.selectOne("order.selectMaxOrderNo");
	}
	
	//(회원) 주문 목록
	public List<OrderVO> selectOrderByPeriod(OrderVO orderVO) {
		return sqlSession.selectList("order.selectOrderByPeriod", orderVO);
	}
	
	//(회원) 주문 상품별 상세 정보
	public List<OrderDetailVO> selectOrderDetailByPeriod(OrderVO orderVO) {
		return sqlSession.selectList("order.selectOrderDetailByPeriod", orderVO);
	}
	
	//(회원) 주문 상품별 1차 옵션 정보
	public OptionVO selectFirstByCartNo(Long cartNo) {
		return sqlSession.selectOne("order.selectFirstByCartNo", cartNo);
	}
	
	//(회원) 주문 상품별 2차 옵션 정보
	public OptionVO selectSecondByCartNo(Long cartNo) {
		return sqlSession.selectOne("order.selectSecondByCartNo", cartNo);
	}
	
	//(회원) 주문 상세 추출
	public OrderDetailVO selectByCartVO(Long cartNo) {
		return sqlSession.selectOne("order.selectByCartNo", cartNo);
	}
	
	//(관리자) 주문 목록
	public List<OrderVO> selectAll(String searchType, String searchKwd){
		Map<String, String> map = new HashMap<>();
		map.put("searchType", searchType);
		map.put("searchKwd", searchKwd);
		map.put("keyValue", keyValue);
		return sqlSession.selectList("order.selectAll", map);
	}
	
	//(관리자) 주문 상태 수정
	public boolean update(OrderVO orderVO){
		return sqlSession.update("order.update", orderVO)==1;
	}
	
}
