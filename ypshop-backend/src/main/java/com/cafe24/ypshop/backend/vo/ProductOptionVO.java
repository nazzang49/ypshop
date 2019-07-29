package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.NotNull;

public class ProductOptionVO {

	private Long no;
	private Long productNo;
	//1차 옵션
	@NotNull
	private Long firstOptionNo;
	//2차 옵션
	@NotNull
	private Long secondOptionNo;
	//재고수량 >> 비재고 -1
	@NotNull
	private Long remainAmount;
	//판매가능수량 >> 비재고 -1
	@NotNull
	private Long availableAmount;
	
	public ProductOptionVO() {
		
	}
	
	public ProductOptionVO(Long productNo, Long firstOptionNo, Long secondOptionNo, Long remainAmount, Long availableAmount) {
		
		this.productNo=productNo;
		this.firstOptionNo=firstOptionNo;
		this.secondOptionNo=secondOptionNo;
		this.remainAmount=remainAmount;
		this.availableAmount=availableAmount;
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
	public Long getFirstOptionNo() {
		return firstOptionNo;
	}
	public void setFirstOptionNo(Long firstOptionNo) {
		this.firstOptionNo = firstOptionNo;
	}
	public Long getSecondOptionNo() {
		return secondOptionNo;
	}
	public void setSecondOptionNo(Long secondOptionNo) {
		this.secondOptionNo = secondOptionNo;
	}
	public Long getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(Long remainAmount) {
		this.remainAmount = remainAmount;
	}
	public Long getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(Long availableAmount) {
		this.availableAmount = availableAmount;
	}

	@Override
	public String toString() {
		return "ProductOptionVO [no=" + no + ", productNo=" + productNo + ", firstOptionNo=" + firstOptionNo
				+ ", secondOptionNo=" + secondOptionNo + ", remainAmount=" + remainAmount + ", availableAmount="
				+ availableAmount + "]";
	}
		
}
