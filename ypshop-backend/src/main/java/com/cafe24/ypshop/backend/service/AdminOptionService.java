package com.cafe24.ypshop.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafe24.ypshop.backend.repository.OptionDAO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.OptionVO;

//(관리자) 옵션 서비스
@Service
public class AdminOptionService {
	
	@Autowired
	private OptionDAO optionDao;
	
	//이미지 목록
	public List<OptionVO> 옵션목록(Long productNo) {
		return optionDao.selectAllOptionByProductNo(productNo);
	}
	
	//옵션 추가
	public String 옵션추가(List<String> optionNameList, List<Long> optionDepthList, Long productNo) {
		StringBuilder returnMsg = new StringBuilder();
		for(int i=0;i<optionNameList.size();i++) {
			String optionName = optionNameList.get(i);
			//중복 X >> 추가 성공 >> 성공 메시지
			if(optionDao.checkExist(productNo, optionName)==null) optionDao.insert(new OptionVO(productNo, optionNameList.get(i), optionDepthList.get(i)));
			//중복 O >> 추가 실패 >> 실패 메시지
			else {
				returnMsg.append((i+1)+"번 ");
				continue;
			}
		}
		if(returnMsg.toString().contains("번")) {
			return returnMsg.append("옵션 추가 실패 >> 중복").toString();
		}
		return returnMsg.append("옵션 추가 성공").toString();
	}
	
	//옵션 삭제
	@Transactional
	public boolean 옵션삭제(List<Long> optionDepthList) {
		for(Long no : optionDepthList) {
			optionDao.delete(no);
		}
		return true;
	}
	
}

