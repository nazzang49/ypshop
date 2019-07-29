package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import com.cafe24.ypshop.backend.config.AppConfig;
import com.cafe24.ypshop.backend.config.TestWebConfig;

//(회원) 컨트롤러 테스트 클래스
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
//@Transactional
//@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@BeforeClass
	public static void setDB() {
	
	}
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();
		
	}
	
	//아이디 중복 체크
//	@Test
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
	
//	@Test
	public void _testAMemberJoin() throws Exception {
		ResultActions resultActions = 
				mockMvc.perform(post("/api/member/join").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")));
	}
	
	//조인
//	@Test
	public void testAMemberJoin() throws Exception {
		//1. success
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
//	@Test
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
		String accessToken = obtainAccessToken("user2", "jy@park2@@");
		
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(get("/api/member/{id}","user2")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.memberVO.id", is("user2")));
	}
	
	//회원수정 >> 아이디 및 권한 수정 불가
	@Test
	public void testEMemberUpdate() throws Exception {
		String accessToken = obtainAccessToken("user2", "jy@park2@@");
		
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(put("/api/member/update")
						.header("Authorization", "Bearer " + accessToken)
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
		String accessToken = obtainAccessToken("user2", "jy@park2@@");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/member/delete/{id}","test")
						.header("Authorization", "Bearer " + accessToken)
						.param("password", "jy@park2@@")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> 
	}
	
	//액세스 토큰 발급
	private String obtainAccessToken(String username, String password) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", "ypshop");
		params.add("username", username);
		params.add("password", password);
		params.add("scope", "USER");
		
		ResultActions resultActions =
		mockMvc
			.perform(post("/oauth/token")
			.params(params)
			.with(httpBasic("ypshop", "1234"))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
		
		String resultString = resultActions.andReturn().getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	@AfterClass
	public static void resetDB() {
		
	}
	
}
