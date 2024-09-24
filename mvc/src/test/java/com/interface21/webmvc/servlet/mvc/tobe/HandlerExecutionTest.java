package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import samples.TestController;

class HandlerExecutionTest {

	@DisplayName("TestController 의 메서드를 실행한다.")
	@Test
	void handleTest() throws Exception {
		var controller = mock(TestController.class);
		var request = mock(HttpServletRequest.class);
		var response = mock(HttpServletResponse.class);
		Method method = TestController.class.getDeclaredMethod(
			"findUserId", HttpServletRequest.class,
			HttpServletResponse.class);

		ModelAndView modelAndView = new ModelAndView(null);
		HandlerExecution handlerExecution = new HandlerExecution(controller, method);
		when(controller.findUserId(request, response)).thenReturn(modelAndView);
		ModelAndView actual = handlerExecution.handle(request, response);

		assertThat(actual).isEqualTo(modelAndView);
		verify(controller).findUserId(request, response);
	}
}
