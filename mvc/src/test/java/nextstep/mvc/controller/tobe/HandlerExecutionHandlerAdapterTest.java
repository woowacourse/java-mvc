package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Nested
    @DisplayName("supports 메소드는")
    class Supports {

        @Test
        @DisplayName("HandlerExecution 객체를 받으면 True를 반환한다.")
        void supports_handlerExecution() {
            // given
            final HandlerExecution handlerExecution = mock(HandlerExecution.class);

            // when
            final boolean canSupport = handlerAdapter.supports(handlerExecution);

            // then
            assertThat(canSupport).isTrue();
        }

        @Test
        @DisplayName("HandlerExecution 이외의 객체를 받으면 False를 반환한다.")
        void supports_notHandlerExecution() {
            // given
            final Controller controller = mock(Controller.class);

            // when
            final boolean canSupport = handlerAdapter.supports(controller);

            // then
            assertThat(canSupport).isFalse();
        }
    }

    @Test
    @DisplayName("handle 메소드는 입력 받은 HandlerExecution의 handle 메소드를 실행하고 결과를 ModelAndView 객체로 반환한다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response)).thenReturn(new ModelAndView(new JspView("/index.html")));

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(result.getView()).isInstanceOf(JspView.class);
    }
}