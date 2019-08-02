package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartVO {

	private Long no;
	private String memberId;
	@NotNull
	private Long productOptionNo;
	//상품별 수량 제한 >> 최소 1개, 최대 10개
	@NotNull
	@Min(value=1, message="최소 수량 1개")
	@Max(value=10, message="최대 수량 10개")
	private Long cartAmount;
	//상품 가격
	private Long cartPrice;
	private Long orderNo;
	//상품명
	private String productName;
	//1차 옵션명
	private String firstOptionName;
	//2차 옵션명
	private String secondOptionName;
	//상품옵션별 재고수량
	private Long remainAmount;
		
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Long getProductOptionNo() {
		return productOptionNo;
	}
	public void setProductOptionNo(Long productOptionNo) {
		this.productOptionNo = productOptionNo;
	}
	public Long getCartAmount() {
		return cartAmount;
	}
	public void setCartAmount(Long cartAmount) {
		this.cartAmount = cartAmount;
	}
	public Long getCartPrice() {
		return cartPrice;
	}
	public void setCartPrice(Long cartPrice) {
		this.cartPrice = cartPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getFirstOptionName() {
		return firstOptionName;
	}
	public void setFirstOptionName(String firstOptionName) {
		this.firstOptionName = firstOptionName;
	}
	public String getSecondOptionName() {
		return secondOptionName;
	}
	public void setSecondOptionName(String secondOptionName) {
		this.secondOptionName = secondOptionName;
	}
	public Long getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(Long remainAmount) {
		this.remainAmount = remainAmount;
	}
	
	@Override
	public String toString() {
		return "CartVO [no=" + no + ", memberId=" + memberId + ", productOptionNo=" + productOptionNo + ", cartAmount="
				+ cartAmount + ", cartPrice=" + cartPrice + ", orderNo=" + orderNo + ", productName=" + productName
				+ ", firstOptionName=" + firstOptionName + ", secondOptionName=" + secondOptionName + ", remainAmount="
				+ remainAmount + "]";
	}
	
}
