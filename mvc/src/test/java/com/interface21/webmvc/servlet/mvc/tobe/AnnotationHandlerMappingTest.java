package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AnnotationHandlerMappingTest {

	private AnnotationHandlerMapping handlerMapping;

	@BeforeEach
	void setUp() throws Exception {
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

	@Test
	void get_withNoMatchingHandler() {
		final var request = mock(HttpServletRequest.class);

		when(request.getRequestURI()).thenReturn("/non-existent");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = handlerMapping.getHandler(request);

		assertThat(handlerExecution).isNull();
	}

	@Test
	void unsupportedMethod() {
		final var request = mock(HttpServletRequest.class);

		when(request.getRequestURI()).thenReturn("/post-test");
		when(request.getMethod()).thenReturn("PUT");

		final var handlerExecution = handlerMapping.getHandler(request);

		assertThat(handlerExecution).isNull();
	}

	@ParameterizedTest
	@ValueSource(strings = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"})
	void noMethodSpecified(String method) throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/no-method-test");
		when(request.getMethod()).thenReturn(method);

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}
}
