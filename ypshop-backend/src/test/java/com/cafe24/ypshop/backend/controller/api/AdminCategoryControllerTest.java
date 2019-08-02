package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();
	}
	
	//카테고리 목록 by 검색 >> name, groupNo
	@Test
	public void testBCategoryListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");

		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/category/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "groupNo")
						.param("searchKwd", "1")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		//1번 카테고리
		.andExpect(jsonPath("$.data.categoryList[0].no", is(1)))
		.andExpect(jsonPath("$.data.categoryList[0].name", is("category1-1")))
		.andExpect(jsonPath("$.data.categoryList[0].depth", is(1)))
		//2번 카테고리
		.andExpect(jsonPath("$.data.categoryList[1].no", is(2)))
		.andExpect(jsonPath("$.data.categoryList[1].name", is("category1-2")))
		.andExpect(jsonPath("$.data.categoryList[1].depth", is(2)));
		
	}
	
	//카테고리 중복 체크
	@Test
	public void _testCategoryCheckExist() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//중복 O
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/category/checkexist")
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "category1-1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//중복 X
		resultActions = 
				mockMvc.perform(get("/api/admin/category/checkexist")
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "category4-1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(false)));
		
	}
	
	//카테고리 추가
	@Test
	public void testACategoryWrite() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.header("Authorization", "Bearer " + accessToken)
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
						.header("Authorization", "Bearer " + accessToken)
						.param("groupNo", "3")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in length of name
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.header("Authorization", "Bearer " + accessToken)
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
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "category3-1category3-1category3-1category3-1category3-1")
						.param("depth", "1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of depth
		resultActions = 
				mockMvc.perform(post("/api/admin/category/add")
						.header("Authorization", "Bearer " + accessToken)
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
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "update-category1-1")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in not empty of name
		resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in length of name
		resultActions = 
				mockMvc.perform(put("/api/admin/category/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "update-category1-1update-category1-1update-category1-1update-category1-1")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//카테고리 삭제
	@Test
	public void testDCategoryDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. fail >> restriction on delete cascade
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in dataType of no (Path Variable)
		resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}", "test")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> FK condition, out of range
		resultActions = 
				mockMvc.perform(delete("/api/admin/category/delete/{no}",100L)
						.header("Authorization", "Bearer " + accessToken)
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
