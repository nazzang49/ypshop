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
	@Min(1)
	@Max(10)
	private Long cartAmount;
	//상품 가격
	private Long cartPrice;
	private Long orderNo;
		
	public CartVO() {
		
	}
	
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
	
	@Override
	public String toString() {
		return "CartVO [no=" + no + ", memberId=" + memberId + ", productOptionNo=" + productOptionNo + ", cartAmount="
				+ cartAmount + ", cartPrice=" + cartPrice + ", orderNo=" + orderNo + "]";
	}
	
}
