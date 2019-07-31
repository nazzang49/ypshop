package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

//(관리자) 회원 관리 컨트롤러 테스트
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminUserControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
		
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();
	}
	
	//회원목록
	@Test
	public void testAUserListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success with search
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/user/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "address")
						.param("searchKwd", "부산")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.memberList[0].id", is("user1")))
		.andExpect(jsonPath("$.data.memberList[1].id", is("user2")));
		
		//1. success without search
		resultActions = 
				mockMvc.perform(get("/api/admin/user/list")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.memberList[0].id", is("user1")))
		.andExpect(jsonPath("$.data.memberList[1].id", is("user2")));
		
		//2. success with no results
		resultActions = 
				mockMvc.perform(get("/api/admin/user/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "address")
						.param("searchKwd", "서울")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(get("/api/admin/user/order/list")
						.param("searchType", "id")
						.param("searchKwd", "user")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//회원 삭제
	@Test
	public void testBUserDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/user/delete")
						.header("Authorization", "Bearer " + accessToken)
						.param("id", "user1")
						.param("id", "user2")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//1. success without deletion
		resultActions = 
				mockMvc.perform(delete("/api/admin/user/delete")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(get("/api/admin/user/order/list")
						.param("searchType", "id")
						.param("searchKwd", "user")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//회원 주문목록
	@Test
	public void testCUserOrderListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success with search
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/user/order/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "id")
						.param("searchKwd", "user")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		//주문 1
		.andExpect(jsonPath("$.data.userOrderList[0].memberId", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[0].memberName", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[0].paymentCategory", is("무통장")))
		//주문 2
		.andExpect(jsonPath("$.data.userOrderList[1].memberId", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[1].memberName", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[1].paymentCategory", is("계좌이체")))
		//주문 3
		.andExpect(jsonPath("$.data.userOrderList[2].memberId", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[2].memberName", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[2].paymentCategory", is("무통장")));
		
		//1. success without search
		resultActions = 
				mockMvc.perform(get("/api/admin/user/order/list")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.userOrderList[0].memberId", is("user2")))
		.andExpect(jsonPath("$.data.userOrderList[1].memberId", is("user2")));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(get("/api/admin/user/order/list")
						.param("searchType", "id")
						.param("searchKwd", "user")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//액세스 토큰 발급
	private String obtainAccessToken(String username, String password, String role) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", "ypshop");
		params.add("username", username);
		params.add("password", password);
		params.add("scope", role);
		
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
	
}
