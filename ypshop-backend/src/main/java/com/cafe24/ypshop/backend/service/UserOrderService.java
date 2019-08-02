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
import com.cafe24.ypshop.backend.repository.ProductOptionDAO;
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
	private CartDAO cartDao;
	
	@Autowired
	private ProductOptionDAO productOptionDao;
		
	//주문 추가 >> 장바구니 번호 >> insert 주문 상세 >> delete 장바구니
	@Transactional
	public String 주문추가(OrderVO orderVO) {
		StringBuilder returnMsg = new StringBuilder();
		
		//1. 각 상품옵션 재고수량 확인
		for(int i=0;i<orderVO.getCartNoList().size();i++) {
			CartVO cartVO = cartDao.selectAmountByNo(orderVO.getCartNoList().get(i));
			
			System.out.println("cartAmount : "+cartVO.getCartAmount());
			System.out.println("remainAmount : "+cartVO.getRemainAmount());
			
			if(cartVO.getCartAmount()>cartVO.getRemainAmount()) {
				returnMsg.append((i+1)+"번 ");
			}
		}
		
		if(returnMsg.toString().contains("번")) {
			returnMsg.append("상품 재고 수량 부족 >> 주문 실패");
			return returnMsg.toString();
		}
		
		
		
		//2. 주문 추가 >> 주문자, 수령자 개인정보 암호화
		orderDao.insert(orderVO);
		Long orderNo = orderDao.selectMaxOrderNo();
		
		//3. 주문 상세 추가 >> 상품 삭제 대비 상품명, 옵션명, 썸네일 경로 중복 입력
		for(Long cartNo : orderVO.getCartNoList()) {
			OrderDetailVO orderDetailVO = orderDao.selectByCartVO(cartNo);
			
			orderDetailVO.setOrderNo(orderNo);
			orderDetailVO.setFirstOptionName(orderDao.selectFirstByCartNo(cartNo).getName());
			orderDetailVO.setSecondOptionName(orderDao.selectSecondByCartNo(cartNo).getName());
			
			orderDao.insertOrderDetail(orderDetailVO);
			
			//주문 완료 후, 장바구니 상품 삭제 및 재고수량 감소
			cartDao.delete(cartNo);
			CartVO cartVO = cartDao.selectAmountByNo(cartNo);
			productOptionDao.updateRemainAmount(cartVO);
		}
		
		returnMsg.append("주문 성공");
		return returnMsg.toString();
	}
	
	//주문 상세 목록
	@Transactional
	public Map<String, Object> 주문상세목록(OrderVO orderVO) {
		Map<String, Object> map = new HashMap<>();
		
		//주문
		List<OrderVO> orderList = orderDao.selectOrderByPeriod(orderVO);
		
		//주문 상품별 옵션명, 썸네일 경로, 상품명, 상품 가격 등 세부 정보
		List<OrderDetailVO> orderDetailList = orderDao.selectOrderDetailByPeriod(orderVO);
		
		map.put("orderList", orderList);
		map.put("orderDetailList", orderDetailList);
		return map;
	}
	
}
