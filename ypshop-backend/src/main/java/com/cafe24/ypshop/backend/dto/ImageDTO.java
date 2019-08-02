package com.cafe24.ypshop.backend.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

//이미지 추가 >> List 타입 @Valid 테스트
public class ImageDTO {

	@NotEmpty
	private String url;
	
	@NotNull
	private Long productNo;
	
	@NotEmpty
	private String repOrBasic;

	public ImageDTO() {
		
	}
	
	public ImageDTO(String url, Long productNo, String repOrBasic) {
		this.url=url;
		this.productNo=productNo;
		this.repOrBasic=repOrBasic;
	}
	
	public String getRepOrBasic() {
		return repOrBasic;
	}
	public void setRepOrBasic(String repOrBasic) {
		this.repOrBasic = repOrBasic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getProductNo() {
		return productNo;
	}
	public void setProductNo(Long productNo) {
		this.productNo = productNo;
	}

	@Override
	public String toString() {
		return "ImageDTO [url=" + url + ", productNo=" + productNo + ", repOrBasic=" + repOrBasic + "]";
	}

}
