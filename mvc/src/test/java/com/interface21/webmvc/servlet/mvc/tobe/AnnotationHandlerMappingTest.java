package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

class AnnotationHandlerMappingTest {

	private Map<HandlerKey, Handler> handlers;
	private AnnotationHandlerMapping handlerMapping;

	@BeforeEach
	void setUp() {
		handlers = new HashMap<>();
		handlerMapping = new AnnotationHandlerMapping(handlers, "samples");
		handlerMapping.initialize();
	}

	@DisplayName("RequestMapping 어노테이션에 method 가 없으면 모든 method 를 등록한다.")
	@Test
	void NoHttpMethod() {
		List<HandlerKey> expected = Arrays.stream(RequestMethod.getRequestMethods())
			.map(requestMethod -> new HandlerKey("/no-method", requestMethod))
			.toList();

		assertThat(handlers.keySet()).containsAll(expected);
	}

	@DisplayName("요청을 처리할 수 있는지 확인한다.")
	@Test
	void canService() {
		final var request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/get-test");
		when(request.getMethod()).thenReturn("GET");

		boolean actual = handlerMapping.canService(request);

		assertThat(actual).isTrue();
	}
}
