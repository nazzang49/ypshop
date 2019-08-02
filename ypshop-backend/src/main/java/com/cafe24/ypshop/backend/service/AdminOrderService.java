package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cafe24.ypshop.backend.repository.OrderDAO;
import com.cafe24.ypshop.backend.vo.OrderVO;

//(관리자) 주문 서비스
@Service
public class AdminOrderService {

	@Autowired
	private OrderDAO orderDao;
	
	//주문 목록
	public List<OrderVO> 주문목록(String searchType, String searchKwd) {
		return orderDao.selectAll(searchType, searchKwd);
	}
	
	//주문 상태 수정
	public boolean 주문상태수정(OrderVO orderVO) {
		return orderDao.update(orderVO);
	}
	
}
