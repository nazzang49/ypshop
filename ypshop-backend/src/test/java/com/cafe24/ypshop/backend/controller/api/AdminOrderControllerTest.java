package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
	
	@BeforeClass
	public static void setDB() {
		
	}
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	//주문 목록
	@Test
	public void testAOrderListRead() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/order/list")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		//list1
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.orderList[0].no", is(1)))
		.andExpect(jsonPath("$.data.orderList[0].memberId", is("test")))
		.andExpect(jsonPath("$.data.orderList[0].customerName", is("박진영")))
		.andExpect(jsonPath("$.data.orderList[0].customerAddress", is("서울")))
		.andExpect(jsonPath("$.data.orderList[0].customerPhone", is("010-1111-1111")))
		.andExpect(jsonPath("$.data.orderList[0].customerEmail", is("test@naver.com")))
		.andExpect(jsonPath("$.data.orderList[0].receiverName", is("박진수")))
		.andExpect(jsonPath("$.data.orderList[0].receiverAddress", is("부산")))
		.andExpect(jsonPath("$.data.orderList[0].receiverPhone", is("010-2222-2222")))
		.andExpect(jsonPath("$.data.orderList[0].receiverMsg", is("부재 시 경비실")))
		.andExpect(jsonPath("$.data.orderList[0].orderDate", is("2018-10-01")))
		.andExpect(jsonPath("$.data.orderList[0].paymentPrice", is(60000)))
		.andExpect(jsonPath("$.data.orderList[0].status", is("입금대기")))
		//list2
		.andExpect(jsonPath("$.data.orderList[1].no", is(2)))
		.andExpect(jsonPath("$.data.orderList[1].memberId", is("test")))
		.andExpect(jsonPath("$.data.orderList[1].customerName", is("박진성")))
		.andExpect(jsonPath("$.data.orderList[1].customerAddress", is("서울")))
		.andExpect(jsonPath("$.data.orderList[1].customerPhone", is("010-1111-1111")))
		.andExpect(jsonPath("$.data.orderList[1].customerEmail", is("test@naver.com")))
		.andExpect(jsonPath("$.data.orderList[1].receiverName", is("박우성")))
		.andExpect(jsonPath("$.data.orderList[1].receiverAddress", is("부산")))
		.andExpect(jsonPath("$.data.orderList[1].receiverPhone", is("010-2222-2222")))
		.andExpect(jsonPath("$.data.orderList[1].receiverMsg", is("부재 시 경비실")))
		.andExpect(jsonPath("$.data.orderList[1].orderDate", is("2018-10-01")))
		.andExpect(jsonPath("$.data.orderList[1].paymentPrice", is(60000)))
		.andExpect(jsonPath("$.data.orderList[1].status", is("입금대기")));
	}
	
	//주문 상태 수정
	@Test
	public void testBOrderUpdate() throws Exception {
		//test >> api
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/order/update/{no}",1L)
						.param("status", "입금완료")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		
		//invalidation in status = 주문상태 입력값 실패 케이스
		resultActions = 
				mockMvc.perform(put("/api/adminorder/update/{no}",1L)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));		
		
	}
	
	@AfterClass
	public static void resetDB() {
		
	}
	
}
