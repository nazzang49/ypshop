package com.cafe24.ypshop.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.repository.CartDAO;
import com.cafe24.ypshop.backend.repository.OptionDAO;
import com.cafe24.ypshop.backend.repository.OrderDAO;
import com.cafe24.ypshop.backend.repository.ProductDAO;
import com.cafe24.ypshop.backend.vo.CartVO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.OptionVO;
import com.cafe24.ypshop.backend.vo.OrderDetailVO;
import com.cafe24.ypshop.backend.vo.OrderVO;

//(회원) 주문 서비스
@Service
public class UserOrderService {

	@Autowired
	private OrderDAO orderDao;
	
	@Autowired
	private OptionDAO optionDao;
	
	@Autowired
	private CartDAO cartDao;
	
	//주문 추가 >> 장바구니 번호 >> insert 주문 상세
	@Transactional
	public boolean 주문추가(OrderVO orderVO) {
		boolean flag = true;
		//1. 주문 추가 >> 주문자, 수령자 개인정보 암호화
		orderDao.insert(orderVO);
		Long orderNo = orderDao.selectMaxOrderNo();
		
		//2. 주문 상세 추가
		for(Long cartNo : orderVO.getCartNoList()) {
			CartVO cartVO = cartDao.selectByNo(cartNo);
			cartVO.setOrderNo(orderNo);
					
			flag = orderDao.insertOrderDetail(cartVO);
		}
		
		return flag;
	}
	
	//주문 상세 목록
	@Transactional
	public Map<String, Object> 주문상세목록(OrderVO orderVO) {
		Map<String, Object> map = new HashMap<>();
		
		//주문
		List<OrderVO> orderList = orderDao.selectOrderByPeriod(orderVO);
		
		//주문 내역 X 
		if(orderList.isEmpty()) {
			map.put("flag", false);
			return map;
		}
		
		//주문 상품별 기본 정보 >> 상품명, 상품가격, 상품번호, 상품옵션 번호, 수량, 대표 이미지
		List<OrderDetailVO> orderDetailList = orderDao.selectOrderDetailByPeriod(orderVO);
		
		//주문 상품별 1차 옵션
		List<OptionVO> firstOptionList = new ArrayList<>();
		
		//주문 상품별 2차 옵션
		List<OptionVO> secondOptionList = new ArrayList<>();
		
		for(int i=0;i<orderDetailList.size();i++) {
			//주문에 대한 각 상품의 1차 옵션 >> 부재 시, -1
			firstOptionList.add(optionDao.selectFirstByProductOptionNo(orderDetailList.get(i).getProductOptionNo()));
			//주문에 대한 각 상품의 2차 옵션 >> 부재 시, -1
			secondOptionList.add(optionDao.selectSecondByProductOptionNo(orderDetailList.get(i).getProductOptionNo()));
		}

		map.put("orderList", orderList);
		map.put("orderDetailList", orderDetailList);
		map.put("firstOptionList", firstOptionList);
		map.put("secondOptionList", secondOptionList);
		
		return map;
	}
	
}
