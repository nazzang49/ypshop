package com.cafe24.ypshop.backend.vo;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

//일단, 기본 정보만 테스트
public class ProductVO {
	
	//상품 기본 정보
	//pk
	private Long no;
	@NotNull
	private Long categoryNo;
	@NotEmpty
	private String name;
	@NotNull
	@Min(0)
	private Long price;
	@NotEmpty
	private String shortDescription;
	@NotEmpty
	private String alignUse;
	private Long alignNo;
	//상품 이미지
	private List<ImageVO> imageList;
	//상품 옵션
	private List<OptionVO> optionList;
	private String ThumbnailUrl;
	
	public ProductVO() {
		
	}
	
	public ProductVO(Long no, String name, Long price, String shortDescription, String alignUse, Long alignNo, Long categoryNo) {
		
		this.no=no;
		this.name=name;
		this.price=price;
		this.shortDescription=shortDescription;
		this.alignUse=alignUse;
		this.alignNo=alignNo;
		this.categoryNo=categoryNo;
	}
	
	public String getThumbnailUrl() {
		return ThumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		ThumbnailUrl = thumbnailUrl;
	}
	public List<ImageVO> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageVO> imageList) {
		this.imageList = imageList;
	}
	public List<OptionVO> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<OptionVO> optionList) {
		this.optionList = optionList;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getAlignUse() {
		return alignUse;
	}
	public void setAlignUse(String alignUse) {
		this.alignUse = alignUse;
	}
	public Long getAlignNo() {
		return alignNo;
	}
	public void setAlignNo(Long alignNo) {
		this.alignNo = alignNo;
	}
	public Long getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(Long categoryNo) {
		this.categoryNo = categoryNo;
	}

	@Override
	public String toString() {
		return "ProductVO [no=" + no + ", categoryNo=" + categoryNo + ", name=" + name + ", price=" + price
				+ ", shortDescription=" + shortDescription + ", alignUse=" + alignUse + ", alignNo=" + alignNo
				+ ", imageList=" + imageList + ", optionList=" + optionList + ", ThumbnailUrl=" + ThumbnailUrl + "]";
	}
}
