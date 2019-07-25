package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithMockUser;
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

//(관리자) 카테고리 관리 컨트롤러 테스트
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminCategoryControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeClass
	public static void setDB() {
		
	}
	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/login")
						.param("id", "user2")
						.param("password", "jy@park2@@")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
	}
	
	//카테고리 목록
	@Test
	@WithMockUser(username="ADMIN")
	public void testBCategoryListRead() throws Exception {
		
		//1. 성공
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/category/list")
						.param("role", "ADMIN")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.categoryList[0].no", is(1)))
		.andExpect(jsonPath("$.data.categoryList[0].name", is("category1-1")))
		.andExpect(jsonPath("$.data.categoryList[0].depth", is(1)))
		.andExpect(jsonPath("$.data.categoryList[1].no", is(2)))
		.andExpect(jsonPath("$.data.categoryList[1].name", is("category1-2")))
		.andExpect(jsonPath("$.data.categoryList[1].depth", is(2)));
		
		//2. 실패 = unauthorization in role
		resultActions = 
				mockMvc.perform(get("/api/admin/category/list")
						.param("role", "USER")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")))
		.andExpect(jsonPath("$.data.authorization", is("permit admin only")));
		

	}
	
	//카테고리 추가
	@Test
	public void testACategoryWrite() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.param("name", "category3-1")
						.param("groupNo", "3")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//invalidation in name = 이름 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//카테고리 수정
	@Test
	public void testCCategoryUpdate() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.param("name", "update-category1-1")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//invalidation in name = 이름 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//카테고리 삭제
	@Test
	public void testDCategoryDelete() throws Exception {
		//카테고리 참조 상품 O >> 삭제 불가
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}",1L)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	@AfterClass
	public static void resetDB() {
		
	}
	
}
