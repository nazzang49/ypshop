package com.cafe24.ypshop.backend.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class CategoryVO {

	private Long no;
	@NotEmpty
	@Length(min=1, max=30)
	private String name;
	@NotNull
	private Long groupNo;
	@NotNull
	@Min(1)
	private Long depth;
	
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
