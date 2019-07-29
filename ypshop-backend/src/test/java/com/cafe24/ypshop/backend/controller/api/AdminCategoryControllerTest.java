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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.cafe24.ypshop.backend.security.SecurityUser;

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
	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	//카테고리 목록
	@Test
	public void testBCategoryListRead() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/category/list")
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

	}
	
	//카테고리 추가
	@Test
	public void testACategoryWrite() throws Exception {
		//1. success
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
		
		//2. fail >> invalidation in not empty of name
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.param("groupNo", "3")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in length of name
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.param("name", "category3-1category3-1category3-1category3-1category3-1")
						.param("groupNo", "3")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of groupNo
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.param("name", "category3-1category3-1category3-1category3-1category3-1")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of depth
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.param("name", "category3-1category3-1category3-1category3-1category3-1")
						.param("groupNo", "3")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//카테고리 수정
	@Test
	public void testCCategoryUpdate() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.param("name", "update-category1-1")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in not empty of name
		resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in length of name
		resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.param("name", "update-category1-1update-category1-1update-category1-1update-category1-1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//카테고리 삭제
	@Test
	public void testDCategoryDelete() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}",7L)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in type of no
		resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}", "test")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> FK
		resultActions = 
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
