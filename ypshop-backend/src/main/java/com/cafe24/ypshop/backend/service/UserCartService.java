package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cafe24.ypshop.backend.repository.CartDAO;
import com.cafe24.ypshop.backend.vo.CartVO;

@Service
public class UserCartService {

	@Autowired
	private CartDAO cartDao;
	
	//장바구니 추가
	public boolean 장바구니추가(CartVO cartVO) {
		return cartDao.insert(cartVO);
	}
	
	//장바구니 목록
	public List<CartVO> 장바구니목록(CartVO cartVO) {
		return cartDao.selectAllByMemberId(cartVO);
	}
	
	//장바구니 수정
	public boolean 장바구니수정(CartVO cartVO) {
		return cartDao.update(cartVO);
	}
	
	//장바구니 삭제
	@Transactional
	public boolean 장바구니삭제(List<Long> cartNoList) {
		for(Long no : cartNoList) cartDao.delete(no);
		return true;
	}
	
}
