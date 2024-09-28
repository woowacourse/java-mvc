package com.techcourse;

import static org.assertj.core.api.Assertions.*;

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

		try {
			dispatcherServlet.service(request, response);
		} catch ( ServletException exception) {
			exception.printStackTrace();
		}

		assertThat(request.getAttribute("username")).isEqualTo("anna");
	}
}
