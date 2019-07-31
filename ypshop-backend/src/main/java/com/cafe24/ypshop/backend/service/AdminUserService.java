package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.repository.MemberDAO;
import com.cafe24.ypshop.backend.repository.OrderDAO;
import com.cafe24.ypshop.backend.vo.MemberVO;
import com.cafe24.ypshop.backend.vo.OrderVO;

//(관리자) 회원 서비스
@Service
public class AdminUserService {

	@Autowired
	private MemberDAO memberDao;
	
	//회원 목록
	public List<MemberVO> 회원목록(String searchType, String searchKwd) {
		return memberDao.selectBySearch(searchType, searchKwd);
	}
	
	//회원 삭제
	@Transactional
	public boolean 회원삭제(List<String> userIdList) {
		for(String id : userIdList) {
			memberDao.deleteByAdmin(id);
		}
		return true;
	}
	
	//회원 주문 목록
	public List<OrderVO> 회원주문목록(String searchType, String searchKwd) {
		return memberDao.selectOrderBySearch(searchType, searchKwd);
	}
	
}
