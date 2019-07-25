package com.cafe24.ypshop.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
@Controller
public class MemberController {

	@RequestMapping("/main")
	public String main() {
		return "main/main";
	}
}
