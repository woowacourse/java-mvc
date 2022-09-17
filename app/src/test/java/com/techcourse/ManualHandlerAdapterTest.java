package com.techcourse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

class ManualHandlerAdapterTest {

	@DisplayName("ManualHandlerAdapter가 지원하는 handler인지 확인한다.")
	@Test
	void supports_true() {
		// given
		Object controller = mock(Controller.class);
		HandlerAdapter handlerAdapter = new ManualHandlerAdapter();

		// when
		boolean result = handlerAdapter.supports(controller);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("AnnotationHandlerAdapter가 지원하지 않는 handler인지 판별한다..")
	@Test
	void supports_false() {
		// given
		Object handlerExecution = mock(HandlerExecution.class);
		HandlerAdapter handlerAdapter = new ManualHandlerAdapter();

		// when
		boolean result = handlerAdapter.supports(handlerExecution);

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("controller를 처리한다.")
	@Test
	void handle() throws Exception {
		// given
		Controller controller = mock(Controller.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		BDDMockito.given(controller.execute(request, response))
			.willReturn("viewName");

		// when
		HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
		ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

		// then
		assertAll(
			() -> assertThat(modelAndView.getView()).isInstanceOf(JspView.class),
			() -> assertThat(((JspView)modelAndView.getView()).getViewName()).isEqualTo("viewName"),
			() -> verify(controller).execute(request, response)
		);

	}
}
