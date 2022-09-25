package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AnnotationHandlerAdapter의")
class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("handler 메서드는 HandlerExecution로 요청을 처리하고 ModelAndView를 반환한다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handler = mock(HandlerExecution.class);

        final ModelAndView expected = new ModelAndView(new JspView(""));
        willReturn(expected)
                .given(handler)
                .handle(request, response);

        // when
        final ModelAndView actual = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Nested
    @DisplayName("supports 메서드는")
    class Supports {

        @Test
        @DisplayName("handler가 HandlerExecution이면 true를 반환한다.")
        void supports_controller_true() {
            // given
            final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
            final HandlerExecution handler = mock(HandlerExecution.class);

            // when
            final boolean actual = handlerAdapter.supports(handler);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("handler가 HandlerExecution이 아니면 false를 반환한다.")
        void supports_notController_true() {
            // given
            final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
            final String handler = "not-controller";

            // when
            final boolean actual = handlerAdapter.supports(handler);

            // then
            assertThat(actual).isFalse();
        }
    }
}
