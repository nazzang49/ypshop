package com.cafe24.ypshop.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cafe24.ypshop.backend.repository.MemberDAO;
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
	public boolean 회원삭제(List<String> userIdList) {
		boolean flag = true;
		
		for(String id : userIdList) {
			flag = memberDao.deleteByAdmin(id);
		}
		return flag;
	}
	
	//회원 주문 목록
	public List<OrderVO> 회원주문목록(String searchType, String searchKwd) {
		return memberDao.selectOrderBySearch(searchType, searchKwd);
	}
	
}
