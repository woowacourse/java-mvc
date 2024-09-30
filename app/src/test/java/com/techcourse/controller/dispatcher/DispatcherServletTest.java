package com.techcourse.controller.dispatcher;

import static org.assertj.core.api.Assertions.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.interface21.webmvc.servlet.dispatcher.DispatcherServlet;

class DispatcherServletTest {

	private DispatcherServlet dispatcherServlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		dispatcherServlet = new DispatcherServlet("com.techcourse");
		dispatcherServlet.init();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@DisplayName("어노테이션 기반으로 매핑된 컨트롤러를 호출한다.")
	@Test
	void mapControllerByAnnotation() {
		request.setMethod("POST");
		request.setRequestURI("/register");
		request.setParameter("account", "gugu");
		request.setParameter("password", "password");
		request.setParameter("email", "gugu@gmail.com");

		try {
			dispatcherServlet.service(request, response);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		assertThat(response.getStatus()).isEqualTo(302);
	}

	@DisplayName("Model에 하나의 값이 저장된 경우 하나의 데이터만 반환된다.")
	@Test
	void returnOneDataWhenModelHaveOneValue() throws UnsupportedEncodingException {
		request.setMethod("GET");
		request.setRequestURI("/api/user");
		request.setParameter("account", "gugu");

		try {
			dispatcherServlet.service(request, response);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		assertThat(response.getContentAsString()).isEmpty();
		assertThat(request.getAttribute("user")).isNotNull();
	}

	@DisplayName("Model에 두 개 이상의 값이 저장된 경우 JSON 데이터를 반환한다.")
	@Test
	void convertJsonInJsonView() throws UnsupportedEncodingException {
		request.setMethod("GET");
		request.setRequestURI("/api/user/detail");
		request.setParameter("account", "gugu");

		try {
			dispatcherServlet.service(request, response);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		String result = response.getContentAsString();
		assertThat(result).isNotEmpty();
		assertThat(result).contains("id");
		assertThat(result).contains("account");
	}
}
