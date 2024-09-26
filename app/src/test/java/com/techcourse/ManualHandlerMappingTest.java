package com.techcourse;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.Handler;

import jakarta.servlet.http.HttpServletRequest;

class ManualHandlerMappingTest {

	private Handler manualHandler;
	private Map<String, Handler> handlers;
	private ManualHandlerMapping manualHandlerMapping;

	@BeforeEach
	void setUp() {
		manualHandler = new ManualHandler((request, response) -> "test");
		handlers = new HashMap<>(Map.of("/get-test", manualHandler));
		manualHandlerMapping = new ManualHandlerMapping(handlers);
	}

	@DisplayName("요청을 처리할 수 있는 Handler 를 찾는다.")
	@Test
	void getHandler() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/get-test");
		when(request.getMethod()).thenReturn("GET");

		Handler actual = manualHandlerMapping.getHandler(request);

		assertThat(actual).isEqualTo(manualHandler);
	}

	@DisplayName("요청을 처리할 수 없다면 null 을 반환한다.")
	@Test
	void getHandlerWhenNoController() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/no-controller");
		when(request.getMethod()).thenReturn("GET");

		Handler actual = manualHandlerMapping.getHandler(request);

		assertThat(actual).isNull();
	}

	@DisplayName("요청을 처리할 수 있는 컨트롤러가 있는지 확인한다.")
	@Test
	void canService() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/get-test");
		when(request.getMethod()).thenReturn("GET");

		boolean actual = manualHandlerMapping.canService(request);

		assertThat(actual).isTrue();
	}
}
