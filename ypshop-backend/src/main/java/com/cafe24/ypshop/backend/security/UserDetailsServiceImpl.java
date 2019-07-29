package com.cafe24.ypshop.backend.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.cafe24.ypshop.backend.repository.MemberDAO;
import com.cafe24.ypshop.backend.vo.MemberVO;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MemberDAO memberDAO;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		MemberVO memberVO = memberDAO.selectSecurityUserById(userName);
		
		SecurityUser securityUser = new SecurityUser();

		if(memberVO!=null) {
			securityUser.setPassword(memberVO.getPassword()); 		//credential
			securityUser.setUsername(memberVO.getId()); 			//principal
			//권한 저장 >> admin 페이지 이동 시 검증 필요
			securityUser.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(memberVO.getRole())));
			
		}
		return securityUser;
	}
}
