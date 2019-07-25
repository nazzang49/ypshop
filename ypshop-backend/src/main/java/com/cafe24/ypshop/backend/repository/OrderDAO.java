package com.cafe24.ypshop.backend.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.ypshop.backend.vo.CartVO;
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
	public boolean insertOrderDetail(CartVO cartVO) {
		return sqlSession.insert("order.insertOrderDetail", cartVO)==1;
	}
	
	//(회원) 주문 번호
	public Long selectMaxOrderNo() {
		return sqlSession.selectOne("order.selectMaxOrderNo");
	}
	
	//(회원) 주문 목록
	public List<OrderVO> selectOrderByPeriod(OrderVO orderVO) {
		return sqlSession.selectList("order.selectOrderByPeriod", orderVO);
	}
	
	//(회원) 주문 상품별 기본 정보
	public List<OrderDetailVO> selectOrderDetailByPeriod(OrderVO orderVO) {
		return sqlSession.selectList("order.selectOrderDetailByPeriod", orderVO);
	}	
	
}
