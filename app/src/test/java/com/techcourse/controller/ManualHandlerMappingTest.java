package com.techcourse.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.techcourse.ManualHandlerMapping;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.asis.Controller;

class ManualHandlerMappingTest {

	private ManualHandlerMapping handlerMapping;
	private static MockedStatic<InMemoryUserRepository> repository;

	@BeforeAll
	static void setStatic() {
		repository = mockStatic(InMemoryUserRepository.class);
	}

	@BeforeEach
	void setUp() {
		handlerMapping = new ManualHandlerMapping();
		handlerMapping.initialize();
	}

	@AfterAll
	static void clear() {
		repository.close();
	}

	@Test
	void login_get_with_session() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);
		final var session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new User(1L, "her0807", "password", "her0807@naver.com"));
		when(request.getRequestURI()).thenReturn("/login");
		when(request.getMethod()).thenReturn("GET");

		final var controller = (Controller)handlerMapping.getHandler(request);
		final var modelAndView = controller.execute(request, response);

		assertThat(modelAndView).isEqualTo("redirect:/index.jsp");
	}

	@Test
	void login_get() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);
		final var session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getRequestURI()).thenReturn("/login");
		when(request.getMethod()).thenReturn("GET");

		final var controller = (Controller)handlerMapping.getHandler(request);
		final var modelAndView = controller.execute(request, response);

		assertThat(modelAndView).isEqualTo("redirect:/login.jsp");
	}

	@Test
	void login_post() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);
		final var session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getParameter("account")).thenReturn("her0807");
		when(InMemoryUserRepository.findByAccount("her0807")).thenReturn(
			Optional.of(new User(1L, "her0807", "password", "her0807@naver.com")));
		when(request.getParameter("password")).thenReturn("password");

		when(request.getRequestURI()).thenReturn("/login");
		when(request.getMethod()).thenReturn("POST");

		final var controller = (Controller)handlerMapping.getHandler(request);
		final var modelAndView = controller.execute(request, response);
		assertThat(modelAndView).isEqualTo("redirect:/index.jsp");
	}

	@Test
	void login_post_false() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);
		final var session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getParameter("account")).thenReturn("her0807");
		when(InMemoryUserRepository.findByAccount("her0807")).thenReturn(
			Optional.of(new User(1L, "her0807", "password", "her0807@naver.com")));
		when(request.getParameter("password")).thenReturn("different_password");

		when(request.getRequestURI()).thenReturn("/login");
		when(request.getMethod()).thenReturn("POST");

		final var controller = (Controller)handlerMapping.getHandler(request);
		final var modelAndView = controller.execute(request, response);
		assertThat(modelAndView).isEqualTo("redirect:/401.jsp");
	}
}
