package com.techcourse.controller.annotation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

class UserControllerTest {

	private UserController userController;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		userController = new UserController();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@DisplayName("유저 정보 요청이 정상적으로 동작한다.")
	@Test
	void show() {
		request.setMethod("GET");
		request.setRequestURI("/api/user");
		request.setParameter("account", "gugu");

		ModelAndView mav = userController.show(request, response);

		Assertions.assertThat(mav.getModel().get("user")).isNotNull();
	}
}
