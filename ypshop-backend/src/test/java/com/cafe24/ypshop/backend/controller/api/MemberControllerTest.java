package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

//(회원) 컨트롤러 테스트 클래스
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberControllerTest {

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
	
	//아이디 중복 체크
	@Test
	public void testBMemberCheckId() throws Exception {
		//test >> api
		//중복 O
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/checkid")
						.param("id", "test")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//중복 X
		resultActions = 
				mockMvc.perform(post("/api/member/checkid")
						.param("id", "test1")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(false)));
	}
	
	@Test
	public void _testAMemberJoin() throws Exception {
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/join").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")));
	}
	
	//조인
	@Test
	public void testAMemberJoin() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/join")
						.param("id", "test")
						.param("name","test")
						.param("password", "jy@park2@@")
						.param("phone", "010-1111-1111")
						.param("email", "test@gmail.com")
						.param("address", "서울")
						.param("role", "USER")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//invalidation in password = 비밀번호 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(post("/api/member/join")
						.param("id", "test")
						.param("name","test")
						.param("password", "test")
						.param("phone", "010-1111-1111")
						.param("email", "test@gmail.com")
						.param("address", "서울")
						.param("role", "USER")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}
	
	//로그인
	@Test
	public void testCMemberLogin() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/login")
						.param("id", "test")
						.param("password","jy@park2@@")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//invalidation in id = 아이디 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(post("/api/member/login")
						.param("password","jy@park2@@")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//회원조회
	@Test
	public void testDMemberRead() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(get("/api/member/{id}","test").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.memberVO.id", is("test")))
		.andExpect(jsonPath("$.data.memberVO.name", is("test")))
		.andExpect(jsonPath("$.data.memberVO.address", is("서울")))
		.andExpect(jsonPath("$.data.memberVO.phone", is("010-1111-1111")))
		.andExpect(jsonPath("$.data.memberVO.email", is("test@gmail.com")))
		.andExpect(jsonPath("$.data.memberVO.role", is("USER")))
		.andExpect(jsonPath("$.data.memberVO.regDate", is("2019-07-18")));
	}
	
	//회원수정 >> 아이디 및 권한 수정 불가
	@Test
	public void testEMemberUpdate() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(put("/api/member/update")
						.param("id", "test")
						.param("name","test-update")
						.param("password", "jy@park2@@")
						.param("phone", "010-1111-1111")
						.param("email", "test@naver.com")
						.param("address", "부산")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//invalidation in password = 비밀번호 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(put("/api/member/update")
						.param("id", "test")
						.param("name","test-update")
						.param("password", "test")
						.param("phone", "010-1111-1111")
						.param("email", "test@naver.com")
						.param("address", "부산")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
		
	//회원탈퇴
	@Test
	public void testFMemberDelete() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/member/delete/{id}","test")
						.param("password", "jy@park2@@")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
	}
	
	@AfterClass
	public static void resetDB() {
		
	}
	
}
