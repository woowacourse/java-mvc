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

	/**
	 * HttpServletRequest 와  HttpServletResponse 에 대해서 데이터 세팅을 위해서 mocking 처리
	 */
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);

	/**
	 * InMemoryUserRepository 는 static 이라서 데이터가 공유되므로 mockStatic 을 사용해야 mocking 이 가능함.
	 */
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
		세션_있음(new User(1L, "her0807", "password", "her0807@naver.com"));

		GET_요청("/login");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/index.jsp");
	}

	@Test
	void login_get() throws Exception {
		세션없음();

		GET_요청("/login");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/login.jsp");
	}

	@Test
	void login_post() throws Exception {
		세션없음();
		로그인_요청_데이터("password");

		POST_요청("/login");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/index.jsp");
	}

	@Test
	void login_post_false() throws Exception {
		세션없음();
		로그인_요청_데이터("different_password");

		POST_요청("/login");

		final String modelAndView = 핸들링_후_반환값();
		assertThat(modelAndView).isEqualTo("redirect:/401.jsp");
	}

	@Test
	void logout() throws Exception {
		세션없음();

		GET_요청("/logout");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/");
	}

	@Test
	void register() throws Exception {
		세션없음();

		GET_요청("/register");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/register.jsp");
	}

	@Test
	void register_post() throws Exception {
		세션없음();
		회원가입_요청_데이터("her0807", "password","her0807@naver.com");
		POST_요청("/register");

		assertThat(핸들링_후_반환값()).isEqualTo("redirect:/index.jsp");
	}

	private void 세션_있음(User value) {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(value);
	}

	private void 세션없음() {
		세션_있음(null);
	}

	private void 로그인_요청_데이터(String password) {
		when(request.getParameter("account")).thenReturn("her0807");
		when(InMemoryUserRepository.findByAccount("her0807")).thenReturn(
			Optional.of(new User(1L, "her0807", "password", "her0807@naver.com")));
		when(request.getParameter("password")).thenReturn(password);
	}

	private void 회원가입_요청_데이터(String account,String password, String email ) {
		when(request.getParameter("account")).thenReturn(account);
		when(request.getParameter("password")).thenReturn(password);
		when(request.getParameter("email")).thenReturn(email);
	}

	private void GET_요청(String url) {
		when(request.getRequestURI()).thenReturn(url);
		when(request.getMethod()).thenReturn("GET");
	}

	private void POST_요청(String url) {
		when(request.getRequestURI()).thenReturn(url);
		when(request.getMethod()).thenReturn("POST");
	}

	private String 핸들링_후_반환값() throws Exception {
		final var controller = (Controller)handlerMapping.getHandler(request);
		return controller.execute(request, response);
	}
}
