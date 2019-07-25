package com.cafe24.ypshop.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	public boolean 옵션추가(List<String> optionNameList, List<Long> optionDepthList, Long productNo) {
		boolean flag = true;
		for(int i=0;i<optionNameList.size();i++) {
			flag = optionDao.insert(new OptionVO(productNo, optionNameList.get(i), optionDepthList.get(i)));
		}
		return flag;
	}
	
	//옵션 삭제
	public boolean 옵션삭제(List<Long> optionDepthList) {
		boolean flag = true;
		for(Long no : optionDepthList) {
			flag = optionDao.delete(no);
		}
		return flag;
	}
	
}

