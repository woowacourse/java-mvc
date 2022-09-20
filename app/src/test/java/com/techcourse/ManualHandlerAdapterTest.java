package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

    @Nested
    @DisplayName("supports 메소드는")
    class Supports {

        @Test
        @DisplayName("Controller 인터페이스 기반의 객체를 받으면 True를 반환한다.")
        void supports_controller() {
            // given
            final Controller controller = mock(Controller.class);

            // when
            final boolean expected = manualHandlerAdapter.supports(controller);

            // then
            assertThat(expected).isTrue();
        }

        @Test
        @DisplayName("Controller 인터페이스 기반이 아닌 객체를 받으면 False를 반환한다.")
        void supports_notController() {
            // given
            final HandlerExecution handlerExecution = mock(HandlerExecution.class);

            // when
            final boolean canSupport = manualHandlerAdapter.supports(handlerExecution);

            // then
            assertThat(canSupport).isFalse();
        }
    }

    @Test
    @DisplayName("")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("/index.html");

        // when
        final ModelAndView result = manualHandlerAdapter.handle(request, response, controller);

        // then
        assertThat(result.getView()).isInstanceOf(JspView.class);
    }
}