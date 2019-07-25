package com.cafe24.ypshop.backend.scenario;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cafe24.ypshop.backend.controller.api.MemberControllerTest;
import com.cafe24.ypshop.backend.controller.api.UserOrderControllerTest;

import junit.framework.Test;
import junit.framework.TestSuite;

//(고객) 주문 시나리오 테스트 >> 회원가입 - 주문 전 과정 순차 테스트
@RunWith(Suite.class)
//별도 작성된 테스트 케이스 취합
@SuiteClasses({MemberControllerTest.class, UserOrderControllerTest.class})
public class UserOrderScenarioTest {

	//순차적으로 MemberControllerTest, UserOrderControllerTest 진행
	public static Test suite() {
		return new TestSuite("회원 주문 시나리오 테스트");
	}
}
