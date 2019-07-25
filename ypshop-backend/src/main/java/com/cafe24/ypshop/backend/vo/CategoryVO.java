package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CategoryVO {

	private Long no;
	@NotEmpty
	private String name;
	@NotNull
	private Long groupNo;
	@NotNull
	private Long depth;
	
	public CategoryVO() {
		
	}
	
	public CategoryVO(Long no, String name, Long groupNo, Long depth) {
		this.no=no;
		this.name=name;
		this.groupNo=groupNo;
		this.depth=depth;
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
	public Long getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
	public Long getDepth() {
		return depth;
	}
	public void setDepth(Long depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "CategoryVO [no=" + no + ", name=" + name + ", groupNo=" + groupNo + ", depth=" + depth + "]";
	}
	
}
