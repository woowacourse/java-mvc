package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AnnotationHandlerMappingTest {

	private AnnotationHandlerMapping handlerMapping;

	@BeforeEach
	void setUp() {
		handlerMapping = new AnnotationHandlerMapping("samples");
		handlerMapping.initialize();
	}

	@Test
	void get() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/get-test");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void post() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/post-test");
		when(request.getMethod()).thenReturn("POST");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@DisplayName("요청 url에 대한 핸들러가 존재하지 않으면 null 핸들러를 반환한다.")
	@Test
	void handlerNotFound() {
		final var request = mock(HttpServletRequest.class);

		when(request.getRequestURI()).thenReturn("/non-existing-url");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = handlerMapping.getHandler(request);

		assertThat(handlerExecution).isNull();
	}

	@DisplayName("요청 url은 같지만, 메서드가 다르면 null 핸들러를 반환한다.")
	@Test
	void methodNotAllowed() {
		final var request = mock(HttpServletRequest.class);

		when(request.getRequestURI()).thenReturn("/post-test");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = handlerMapping.getHandler(request);

		assertThat(handlerExecution).isNull();
	}

	@DisplayName("핸들러의 정상 등록을 확인한다.")
	@Test
	void initialize() throws Exception {
		Class<?> clazz = handlerMapping.getClass();

		Field handlerExecutions = clazz.getDeclaredField("handlerExecutions");
		handlerExecutions.setAccessible(true);

		Map<?, ?> executions = (Map<?, ?>)handlerExecutions.get(handlerMapping);

		assertThat(executions.size()).isEqualTo(2);
		assertThat(executions.keySet())
			.extracting("url")
			.containsExactlyInAnyOrder("/get-test", "/post-test");
	}
}
