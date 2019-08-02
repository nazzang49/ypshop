package com.cafe24.ypshop.backend.controller.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
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
import com.cafe24.ypshop.backend.dto.ImageDTO;
import com.cafe24.ypshop.backend.vo.ImageVO;
import com.cafe24.ypshop.backend.vo.OptionVO;
import com.cafe24.ypshop.backend.vo.ProductVO;
import com.google.gson.Gson;

//(관리자) 상품 관리 컨트롤러 테스트
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminProductControllerTest {

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
	
	//상품 목록 by 검색 >> 상품명, 카테고리 번호, 상품 설명, 진열 구분
	@Test
	public void testBProductListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//카테고리 >> 진열번호 desc
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/product/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("categoryNo", "1")
						.param("searchType", "name")
						.param("searchKwd", "product")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.productList[0].no", is(1)))
		.andExpect(jsonPath("$.data.productList[0].name", is("product1")));
		
		//전체 >> 상품번호 desc
		resultActions = 
				mockMvc.perform(get("/api/admin/product/list")
						.header("Authorization", "Bearer " + accessToken)
						.param("searchType", "name")
						.param("searchKwd", "product")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.productList[0].no", is(5)))
		.andExpect(jsonPath("$.data.productList[1].no", is(4)));
	}
	
	//상품 추가
	@Test
	public void testAProductWrite() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		ProductVO productVO = new ProductVO();
		productVO.setName("product6");
		productVO.setCategoryNo(1L);
		productVO.setPrice(20000L);
		productVO.setShortDescription("설명6");
		productVO.setAlignUse("Y");
		
		List<ImageVO> imageList = new ArrayList<>();
		imageList.add(new ImageVO(6L, "image11", "R"));
		imageList.add(new ImageVO(6L, "image12", "B"));
		
		productVO.setImageList(imageList);
		
		List<OptionVO> optionList = new ArrayList<>();
		optionList.add(new OptionVO(6L, "black", 1L));
		optionList.add(new OptionVO(6L, "M", 2L));
		
		productVO.setOptionList(optionList);
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/product/add")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(productVO)));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> data integrity violate exception
		productVO.setCategoryNo(8L);
		
		resultActions = 
				mockMvc.perform(post("/api/admin/product/add")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(productVO)));

		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not empty of name
		productVO.setName(null);
		
		resultActions = 
				mockMvc.perform(post("/api/admin/product/add")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(productVO)));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of price
		productVO.setName("product6");
		productVO.setPrice(null);
		
		resultActions = 
				mockMvc.perform(post("/api/admin/product/add")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(productVO)));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not empty of shortDescription
		productVO.setName("product6");
		productVO.setPrice(20000L);
		productVO.setShortDescription(null);
		
		resultActions = 
				mockMvc.perform(post("/api/admin/product/add")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(productVO)));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//상품 수정
	@Test
	public void testCProductUpdate() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(put("/api/admin/product/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "product1-update")
						.param("categoryNo", "2")
						.param("shortDescription", "설명1-update")
						.param("price", "30000")
						.param("alignUse", "Y")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in not null of categoryNo
		resultActions = 
				mockMvc.perform(put("/api/admin/product/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "product1-update")
						.param("shortDescription", "설명1-update")
						.param("price", "30000")
						.param("alignUse", "Y")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of price
		resultActions = 
				mockMvc.perform(put("/api/admin/product/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "product1-update")
						.param("categoryNo", "2")
						.param("shortDescription", "설명1-update")
						.param("alignUse", "Y")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in dataType of price
		resultActions = 
				mockMvc.perform(put("/api/admin/product/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "product1-update")
						.param("categoryNo", "2")
						.param("shortDescription", "설명1-update")
						.param("price", "invalidation in dataType")
						.param("alignUse", "Y")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not empty of alignUse
		resultActions = 
				mockMvc.perform(put("/api/admin/product/update/{no}",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "product1-update")
						.param("categoryNo", "2")
						.param("shortDescription", "설명1-update")
						.param("price", "30000")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//상품 삭제 >> 주문상세 set null, 그 외 cascade
	@Test
	public void testDProductDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/product/delete/{no}",3L)
						.header("Authorization", "Bearer " + accessToken)
						.param("categoryNo", "3")
						.param("alignNo", "3")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in not null of categoryNo
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/delete/{no}",3L)
						.header("Authorization", "Bearer " + accessToken)
						.param("alignNo", "3")	
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//이미지 추가
//	@Test
	public void testEImageWrite() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		List<ImageDTO> imageDtoList = new ArrayList<>();
		
		ImageDTO imageDTO = new ImageDTO();
		imageDtoList.add(new ImageDTO("image11", 1L, "B"));
		imageDtoList.add(new ImageDTO("image12", 1L, "B"));
		imageDtoList.add(new ImageDTO("image13", 1L, "B"));
				
		//2. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/image/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(imageDtoList)));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.returnMsg", is("이미지 추가 성공")));
		
		//2. fail >> duplication
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/image/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(imageDtoList)));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.returnMsg", is("1번 2번 3번 이미지 추가 실패 >> 중복")));
		
		imageDtoList.add(new ImageDTO(null, 1L, "B"));
		
		//2. fail >> invalidation in not empty of url
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/image/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(imageDtoList)));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}
	
	//이미지 목록
//	@Test
	public void testIImageListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/product/{productNo}/image/list", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.imageList[0].url", is("image6")))
		.andExpect(jsonPath("$.data.imageList[0].repOrBasic", is("B")))
		.andExpect(jsonPath("$.data.imageList[1].url", is("image1")))
		.andExpect(jsonPath("$.data.imageList[1].repOrBasic", is("R")));
		
		//2. fail >> invalidation in dataType of productNo
		resultActions = 
				mockMvc.perform(get("/api/admin/product/{productNo}/image/list", "test")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}
	
	//이미지 삭제
//	@Test
	public void testFImageDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/product/image/delete",1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("no", "1")
						.param("no", "6")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. success with no results
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/image/delete",1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/image/delete",1L)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//옵션 목록
//	@Test
	public void testJOptionListRead() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(get("/api/admin/product/{productNo}/option/list", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.optionList[0].name", is("XL")))
		.andExpect(jsonPath("$.data.optionList[1].name", is("L")))
		.andExpect(jsonPath("$.data.optionList[2].name", is("black")));
		
		//2. fail >> invalidation in dataType of productNo
		resultActions = 
				mockMvc.perform(get("/api/admin/product/{productNo}/option/list", "test")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	//옵션 추가
//	@Test
	public void testGOptionWrite() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/option/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "pink")
						.param("depth", "1")
						.param("name", "XXL")
						.param("depth", "2")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.returnMsg", is("옵션 추가 성공")));
		
		//2. fail >> invalidation in dataType of depth
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/option/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "pink")
						.param("depth", "test")
						.param("name", "XXL")
						.param("depth", "2")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> duplication
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/option/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "black")
						.param("depth", "1")
						.param("name", "L")
						.param("depth", "2")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.returnMsg", is("1번 2번 옵션 추가 실패 >> 중복")));
		
		//2. fail >> invalidation in dataType of productNo
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/option/add", "test")
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "pink")
						.param("depth", "1")
						.param("name", "XXL")
						.param("depth", "2")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in not null of depth
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/option/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("name", "pink")
						.param("name", "XXL")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print());
		
	}
	
	//옵션 삭제
	@Test
	public void testHOptionDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/product/option/delete")
						.header("Authorization", "Bearer " + accessToken)
						.param("no", "1")
						.param("no", "2")
						.param("no", "3")
						.param("no", "4")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//1. success without deletion
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/option/delete")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> invalidation in dataType of no
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/option/delete")
						.header("Authorization", "Bearer " + accessToken)
						.param("no", "test")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/option/delete")
						.param("no", "7")
						.param("no", "8")
						.param("no", "9")
						.param("no", "10")
						.contentType(MediaType.APPLICATION_JSON));
		
		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	//상품옵션 추가
	@Test
	public void testKProductOptionWrite() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/productOption/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.param("firstOptionNo", "1")
						.param("firstOptionNo", "1")
						.param("secondOptionNo", "6")
						.param("secondOptionNo", "7")
						.param("remainAmount", "300")
						.param("remainAmount", "300")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//2. fail >> data integrity violation exception 500
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/productOption/add", 1L)
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> invalidation in dataType of productNo
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/productOption/add", "test")
						.header("Authorization", "Bearer " + accessToken)
						.param("firstOptionNo", "1")
						.param("firstOptionNo", "1")
						.param("secondOptionNo", "6")
						.param("secondOptionNo", "7")
						.param("remainAmount", "300")
						.param("remainAmount", "300")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));

		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(post("/api/admin/product/{productNo}/productOption/add", 1L)
						.param("firstOptionNo", "1")
						.param("firstOptionNo", "1")
						.param("secondOptionNo", "6")
						.param("secondOptionNo", "7")
						.param("remainAmount", "300")
						.param("remainAmount", "300")
						.contentType(MediaType.APPLICATION_JSON));

		resultActions
		.andExpect(status().isUnauthorized()).andDo(print());
		
	}
	
	//상품옵션 삭제
	@Test
	public void testLProductOptionDelete() throws Exception {
		String accessToken = obtainAccessToken("user1", "jy@park2@@", "ADMIN");
		
		//1. success
		ResultActions resultActions = 
				mockMvc.perform(delete("/api/admin/product/productOption/delete")
						.header("Authorization", "Bearer " + accessToken)
						.param("no", "1")
						.param("no", "2")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.flag", is(true)));
		
		//1. success without deletion
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/productOption/delete")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.result", is("success")));
		
		//2. fail >> invalidation of dataType of no
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/productOption/delete")
						.header("Authorization", "Bearer " + accessToken)
						.param("no", "test")
						.param("no", "2")
						.contentType(MediaType.APPLICATION_JSON));
	
		resultActions
		.andExpect(status().isBadRequest()).andDo(print())
		.andExpect(jsonPath("$.result", is("fail")));
		
		//2. fail >> unauthorization
		resultActions = 
				mockMvc.perform(delete("/api/admin/product/productOption/delete")
						.param("no", "1")
						.param("no", "2")
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
