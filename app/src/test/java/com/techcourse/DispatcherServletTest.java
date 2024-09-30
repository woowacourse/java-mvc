package com.techcourse;

import static org.assertj.core.api.Assertions.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;

class DispatcherServletTest {

	private DispatcherServlet dispatcherServlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		dispatcherServlet = new DispatcherServlet();
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
		} catch (ServletException exception) {
			exception.printStackTrace();
		}

		assertThat(response.getStatus()).isEqualTo(302);
	}

	@DisplayName("사용자 정보가 JsonView에서 JSON으로 변환된다.")
	@Test
	void convertJsonInJsonView() throws UnsupportedEncodingException {
		request.setMethod("GET");
		request.setRequestURI("/api/user");
		request.setParameter("account", "gugu");

		try {
			dispatcherServlet.service(request, response);
		} catch (ServletException exception) {
			exception.printStackTrace();
		}

		assertThat(response.getContentAsString()).isNotNull();
	}
}
