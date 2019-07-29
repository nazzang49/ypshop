package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class OptionVO {

	private Long no;
	private Long productNo;
	@NotEmpty
	private String name;
	@NotNull
	private Long depth;
	
	public OptionVO() {

	}
	
	public OptionVO(Long productNo, String name, Long depth) {
		this.productNo=productNo;
		this.name=name;
		this.depth=depth;
	}
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public Long getProductNo() {
		return productNo;
	}
	public void setProductNo(Long productNo) {
		this.productNo = productNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDepth() {
		return depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "OptionVO [no=" + no + ", productNo=" + productNo + ", name=" + name + ", depth=" + depth + "]";
	}
}