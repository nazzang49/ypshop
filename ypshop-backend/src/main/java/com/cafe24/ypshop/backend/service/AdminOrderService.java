package com.cafe24.ypshop.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cafe24.ypshop.backend.vo.OrderVO;

//(관리자) 주문 서비스
@Service
public class AdminOrderService {

	//주문 DB
	private static List<OrderVO> orderTable = new ArrayList<>();
	
	//DB 초기화
	public void initTables() {
		orderTable.clear();
		orderTable.add(new OrderVO(1L, "test", "박진영", "서울", "010-1111-1111", "test@naver.com", "박진수", "부산", "010-2222-2222", "부재 시 경비실", "2018-10-01", 60000L, "입금대기"));
		orderTable.add(new OrderVO(2L, "test", "박진성", "서울", "010-1111-1111", "test@naver.com", "박우성", "부산", "010-2222-2222", "부재 시 경비실", "2018-10-01", 60000L, "입금대기"));
	}
	
	//test by 하드코딩
	//주문 목록
	public List<OrderVO> 주문목록() {
		initTables();
		return orderTable;
	}
	
	//test by 하드코딩
	//주문 상태 수정
	public boolean 주문상태수정(OrderVO ovo) {
		initTables();
		if(ovo!=null) {
			for(OrderVO tvo : orderTable) {
				if(tvo.getNo()==ovo.getNo()) return true;
			}
		}
		return false;
	}
	
}
