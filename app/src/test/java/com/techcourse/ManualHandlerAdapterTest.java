package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @DisplayName("Controller 객체가 맞다면 true를 반환한다.")
    @Test
    void supports_returnsTrue_ifHandlerExecution() {
        // given
        final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        final Controller controller = mock(Controller.class);

        // when
        final boolean expected = handlerAdapter.supports(controller);

        // then
        assertThat(expected).isTrue();
    }

    @DisplayName("Handler 객체가 아니라면 false를 반환한다.")
    @Test
    void supports_returnsFalse_ifNotHandlerExecution() {
        // given
        final HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        final boolean expected = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void handle() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);
        when(controller.execute(request, response)).thenReturn("name");

        // when
        final ModelAndView expected = handlerAdapter.handle(request, response, controller);

        // then
        assertThat(expected).isNotNull();
    }
}
