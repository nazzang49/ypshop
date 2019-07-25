package com.cafe24.ypshop.backend.vo;

public class OrderDetailVO {

	private Long no;
	private Long orderNo;
	private Long productOptionNo;
	//상품별 주문 수량
	private Long orderAmount;
	//상품가격
	private Long orderPrice;
	//상품명
	private String productName;
	//상품번호
	private Long productNo;
	//대표 이미지
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Long getProductNo() {
		return productNo;
	}
	public void setProductNo(Long productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Long getProductOptionNo() {
		return productOptionNo;
	}
	public void setProductOptionNo(Long productOptionNo) {
		this.productOptionNo = productOptionNo;
	}
	public Long getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Long getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Long orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@Override
	public String toString() {
		return "OrderDetailVO [no=" + no + ", orderNo=" + orderNo + ", productOptionNo=" + productOptionNo
				+ ", orderAmount=" + orderAmount + ", orderPrice=" + orderPrice + ", productName=" + productName
				+ ", productNo=" + productNo + ", imageUrl=" + imageUrl + "]";
	}
	
}
