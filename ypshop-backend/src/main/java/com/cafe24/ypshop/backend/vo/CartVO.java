package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.NotNull;

public class CartVO {

	private Long no;
	private String memberId;
	@NotNull
	private Long productOptionNo;
	//상품별 수량
	@NotNull
	private Long cartAmount;
	//상품 가격
	private Long cartPrice;
	private Long orderNo;
		
	public CartVO() {
		
	}
	
	public CartVO(Long no, String memberId, Long productOptionNo, Long cartAmount, Long cartPrice) {
		this.no=no;
		this.memberId=memberId;
		this.productOptionNo=productOptionNo;
		this.cartAmount=cartAmount;
		this.cartPrice=cartPrice;
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
