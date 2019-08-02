package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

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
import com.cafe24.ypshop.backend.vo.CartVO;
import com.google.gson.Gson;

//(회원) 주문 컨트롤러 테스트 클래스
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserOrderControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	//장바구니 추가 >> 최대 수량 제한
	@Test
	public void testACartWrite() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/order/cart/add")
						.param("memberId", "user2")
						.param("productOptionNo", "2")
						.param("cartAmount", "5")
						.param("cartPrice", "30000")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));

		//2. fail >> invalidation in max of cartAmount
		resultActions = 
				mockMvc.perform(post("/api/order/cart/add")
						.param("memberId", "user2")
						.param("productOptionNo", "2")
						.param("cartAmount", "11")
						.param("cartPrice", "30000")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of cartAmount
		resultActions = 
				mockMvc.perform(post("/api/order/cart/add")
						.param("memberId", "user2")
						.param("productOptionNo", "2")
						.param("cartPrice", "30000")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in dataType of cartAmount
		resultActions = 
				mockMvc.perform(post("/api/order/cart/add")
						.param("memberId", "user2")
						.param("productOptionNo", "2")
						.param("cartAmount", "test")
						.param("cartPrice", "30000")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of productOptionNo
		resultActions = 
				mockMvc.perform(post("/api/order/cart/add")
						.param("memberId", "user2")
						.param("cartAmount", "11")
						.param("cartPrice", "30000")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//장바구니 목록
	@Test
	public void testBCartListRead() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/order/cart/{memberId}","user2").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		//1번 장바구니
		.andExpect(jsonPath("$.data.cartList[0].productOptionNo", is(1)))
		.andExpect(jsonPath("$.data.cartList[0].productName", is("product1")))
		.andExpect(jsonPath("$.data.cartList[0].firstOptionName", is("black")))
		.andExpect(jsonPath("$.data.cartList[0].secondOptionName", is("L")))
		//2번 장바구니
		.andExpect(jsonPath("$.data.cartList[1].productOptionNo", is(2)))
		.andExpect(jsonPath("$.data.cartList[1].productName", is("product2")))
		.andExpect(jsonPath("$.data.cartList[1].firstOptionName", is("white")))
		.andExpect(jsonPath("$.data.cartList[1].secondOptionName", is("M")));
		
		//1. success with no results
		resultActions = 
				mockMvc.perform(get("/api/order/cart/{memberId}", "user1").contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")));
	}
	
	//장바구니 수량, 상품옵션 수정
	@Test
	public void testCCartUpdate() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(put("/api/order/cart/update/{no}",1L)
						.param("productOptionNo", "6")
						.param("cartAmount", "5")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in not null cartAmount
		resultActions = 
				mockMvc.perform(put("/api/order/cart/update/{no}",1L)
						.param("productOptionNo", "3")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in min cartAmount
		resultActions = 
				mockMvc.perform(put("/api/order/cart/update/{no}",1L)
						.param("productOptionNo", "0")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in max cartAmount
		resultActions = 
				mockMvc.perform(put("/api/order/cart/update/{no}",1L)
						.param("productOptionNo", "11")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of productOptionNo
		resultActions = 
				mockMvc.perform(put("/api/order/cart/update/{no}",1L)
						.param("cartAmount", "5")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}
	
	//장바구니 삭제
	@Test
	public void testDCartDelete() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/order/cart/delete")
						.param("no", "1")
						.param("no", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//1. success without deletion
		resultActions = 
				mockMvc.perform(delete("/api/order/cart/delete")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation dataType of no
		resultActions = 
				mockMvc.perform(delete("/api/order/cart/delete")
						.param("no", "test")
						.param("no", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	
	}
	
	//주문 + 주문 상세 추가
	@Test
	public void testFOrderByCartWrite() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/order/add")
						.param("memberId", "user2")
						.param("customerName", "박진영")
						.param("customerAddress", "미국")
						.param("customerPhone", "01011111111")
						.param("customerEmail", "user2@naver.com")
						.param("receiverName", "박우성")
						.param("receiverAddress", "한국")
						.param("receiverPhone", "01022222222")
						.param("receiverMsg", "부재 시 경비실")
						.param("paymentCategory", "계좌이체")
						.param("paymentPrice", "400000")
						//장바구니 번호
						.param("cartNoList", "1")
						.param("cartNoList", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.returnMsg", is("주문 성공")));
		
		//2. fail >> invalidation in pattern of customerName
		resultActions = 
				mockMvc.perform(post("/api/order/add")
						.param("memberId", "user2")
						.param("customerName", "#박진영")
						.param("customerAddress", "미국")
						.param("customerPhone", "01011111111")
						.param("customerEmail", "user2@naver.com")
						.param("receiverName", "박우성")
						.param("receiverAddress", "한국")
						.param("receiverPhone", "01022222222")
						.param("receiverMsg", "부재 시 경비실")
						.param("paymentCategory", "계좌이체")
						.param("paymentPrice", "400000")
						//장바구니 번호
						.param("cartNoList", "1")
						.param("cartNoList", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in pattern of customerPhone
		resultActions = 
				mockMvc.perform(post("/api/order/add")
						.param("memberId", "user2")
						.param("customerName", "박진영")
						.param("customerAddress", "미국")
						.param("customerPhone", "010-1111-1111")
						.param("customerEmail", "user2@naver.com")
						.param("receiverName", "박우성")
						.param("receiverAddress", "한국")
						.param("receiverPhone", "01022222222")
						.param("receiverMsg", "부재 시 경비실")
						.param("paymentCategory", "계좌이체")
						.param("paymentPrice", "400000")
						//장바구니 번호
						.param("cartNoList", "1")
						.param("cartNoList", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in email of customerEmail
		resultActions = 
				mockMvc.perform(post("/api/order/add")
						.param("memberId", "user2")
						.param("customerName", "박진영")
						.param("customerAddress", "미국")
						.param("customerPhone", "01011111111")
						.param("customerEmail", "user2#naver.com")
						.param("receiverName", "박우성")
						.param("receiverAddress", "한국")
						.param("receiverPhone", "01022222222")
						.param("receiverMsg", "부재 시 경비실")
						.param("paymentCategory", "계좌이체")
						.param("paymentPrice", "400000")
						//장바구니 번호
						.param("cartNoList", "1")
						.param("cartNoList", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not empty of receiverAddress
		resultActions = 
				mockMvc.perform(post("/api/order/add")
						.param("memberId", "user2")
						.param("customerName", "박진영")
						.param("customerAddress", "미국")
						.param("customerPhone", "01011111111")
						.param("customerEmail", "user2@naver.com")
						.param("receiverName", "박우성")
						.param("receiverPhone", "01022222222")
						.param("receiverMsg", "부재 시 경비실")
						.param("paymentCategory", "계좌이체")
						.param("paymentPrice", "400000")
						//장바구니 번호
						.param("cartNoList", "1")
						.param("cartNoList", "2")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//주문 상세 목록 >> 기본 3개월 조회
	@Test
	public void testGOrderDetailListRead() throws Exception {
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/order/list/{memberId}","user2")
						.param("period", "-3")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		//주문 번호
		.andExpect(jsonPath("$.data.orderList[0].no", is(1)))
		.andExpect(jsonPath("$.data.orderList[0].orderDate", is("2019-07-31")))
		//1번 주문 1번 상품
		.andExpect(jsonPath("$.data.orderDetailList[0].orderNo", is(1)))
		.andExpect(jsonPath("$.data.orderDetailList[0].productName", is("product1")))
		.andExpect(jsonPath("$.data.orderDetailList[0].imageUrl", is("image1")))
		.andExpect(jsonPath("$.data.orderDetailList[0].orderAmount", is(5)))
		.andExpect(jsonPath("$.data.orderDetailList[0].firstOptionName", is("black")))
		.andExpect(jsonPath("$.data.orderDetailList[0].secondOptionName", is("L")))
		//1번 주문 2번 상품
		.andExpect(jsonPath("$.data.orderDetailList[1].orderNo", is(1)))
		.andExpect(jsonPath("$.data.orderDetailList[1].productName", is("product2")))
		.andExpect(jsonPath("$.data.orderDetailList[1].imageUrl", is("image2")))
		.andExpect(jsonPath("$.data.orderDetailList[1].orderAmount", is(10)))
		.andExpect(jsonPath("$.data.orderDetailList[1].firstOptionName", is("white")))
		.andExpect(jsonPath("$.data.orderDetailList[1].secondOptionName", is("M")))
		
		//주문 번호
		.andExpect(jsonPath("$.data.orderList[1].no", is(2)))
		.andExpect(jsonPath("$.data.orderList[1].orderDate", is("2019-07-31")))
		//2번 주문 1번 상품
		.andExpect(jsonPath("$.data.orderDetailList[2].orderNo", is(2)))
		.andExpect(jsonPath("$.data.orderDetailList[2].productName", is("product3")))
		.andExpect(jsonPath("$.data.orderDetailList[2].imageUrl", is("image3")))
		.andExpect(jsonPath("$.data.orderDetailList[2].orderAmount", is(3)))
		.andExpect(jsonPath("$.data.orderDetailList[2].firstOptionName", is("green")))
		.andExpect(jsonPath("$.data.orderDetailList[2].secondOptionName", is("S")))
		//2번 주문 2번 상품
		.andExpect(jsonPath("$.data.orderDetailList[3].orderNo", is(2)))
		.andExpect(jsonPath("$.data.orderDetailList[3].productName", is("product4")))
		.andExpect(jsonPath("$.data.orderDetailList[3].imageUrl", is("image4")))
		.andExpect(jsonPath("$.data.orderDetailList[3].orderAmount", is(2)))
		.andExpect(jsonPath("$.data.orderDetailList[3].firstOptionName", is("blue")))
		.andExpect(jsonPath("$.data.orderDetailList[3].secondOptionName", is("XS")));
		
	}
		
}

