package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

//(관리자) 주문 관리 컨트롤러 테스트
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminOrderControllerTest {

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
	
	//주문 목록 by 검색 >> 아이디, 주문일, 주문상태, 주문자 이름, 수령자 이름
	@Test
	public void testAOrderListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/order/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "memberId")
						.param("searchKwd", "user2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		//주문1
		.andExpect(jsonPath("$.data.orderList[0].no", is(1)))
		.andExpect(jsonPath("$.data.orderList[0].memberId", is("user2")))
		.andExpect(jsonPath("$.data.orderList[0].orderDate", is("2019-07-31")))
		.andExpect(jsonPath("$.data.orderList[0].paymentPrice", is(400000)))
		.andExpect(jsonPath("$.data.orderList[0].status", is("주문 확인")))
		//주문2
		.andExpect(jsonPath("$.data.orderList[1].no", is(2)))
		.andExpect(jsonPath("$.data.orderList[1].memberId", is("user2")))
		.andExpect(jsonPath("$.data.orderList[1].orderDate", is("2019-07-31")))
		.andExpect(jsonPath("$.data.orderList[1].paymentPrice", is(220000)))
		.andExpect(jsonPath("$.data.orderList[1].status", is("주문 확인")));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(get("/api/admin/order/list")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//주문 상태 수정
	@Test
	public void testBOrderUpdate() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/order/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("status", "입금 확인")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		
		//2. fail >> unauthorization 
		resultActions = 
				mockMvc.perform(put("/api/admin/order/update/{no}",1L)
						.param("status", "입금 확인")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
		
		//2. fail >> enum condition
		resultActions = 
				mockMvc.perform(put("/api/admin/order/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("status", "ENUM 타입 위반")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
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
