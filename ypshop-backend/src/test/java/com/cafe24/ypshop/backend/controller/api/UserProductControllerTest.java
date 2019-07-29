package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.cafe24.ypshop.backend.config.AppConfig;
import com.cafe24.ypshop.backend.config.TestWebConfig;
import com.cafe24.ypshop.backend.vo.MemberVO;
import com.google.gson.Gson;

//(회원) 상품 컨트롤러 테스트 클래스
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeClass
	public static void setDB() {
		
	}
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	//(회원) 상품 목록 by 카테고리 번호
	@Test
	public void testAProductListRead() throws Exception {
		//카테고리 O >> 진열번호 desc
		ResultActions resultActions = 
				mockMvc.perform(get("/api/product/list/{categoryNo}", "test").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.productList[0].no", is(2)))
		.andExpect(jsonPath("$.data.productList[1].no", is(1)));
		
		//카테고리 X >> 상품번호 desc
		resultActions = 
				mockMvc.perform(get("/api/product/list").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.productList[0].no", is(2)))
		.andExpect(jsonPath("$.data.productList[1].no", is(1)));
		
		//fail >> 범위 초과
		resultActions = 
				mockMvc.perform(get("/api/product/list/{categoryNo}", 10000L).contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")))
		.andExpect(jsonPath("$.data.returnPage", is("redirect:/api/product/list")));
		
		//상품 없음
//		resultActions = 
//				mockMvc.perform(get("/api/product/list").contentType(MediaType.APPLICATION_JSON));
//		
//		resultActions
//		.andExpect(status().isOk()).andDo(print())
//		.andExpect(jsonPath("$.result", is("success")))
//		.andExpect(jsonPath("$.data.alert", is("None of products is available")));
	}
	
	//(회원) 상품 상세 >> 상품 기본, 이미지, 옵션
	@Test
	public void testBProductViewRead() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/product/view/{no}", 1L).contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.pvo.no", is(1)));
		
		//2. fail >> exception in dataType of no
		resultActions = 
				mockMvc.perform(get("/api/product/view/{no}", "test").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//범위 초과
//		resultActions = 
//				mockMvc.perform(get("/api/product/detail/{no}",99999L).contentType(MediaType.APPLICATION_JSON));
//		
//		resultActions
//		.andExpect(status().isOk()).andDo(print())
//		.andExpect(jsonPath("$.result", is("success")))
//		.andExpect(jsonPath("$.data.returnPage", is("redirect:/api/product/list")));
	}
	
	@AfterClass
	public static void resetDB() {
		
	}
	
	
}
