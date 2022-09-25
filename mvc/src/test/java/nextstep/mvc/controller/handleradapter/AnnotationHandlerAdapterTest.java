package nextstep.mvc.controller.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestManualController;

@DisplayName("AnnotationHandlerAdapter 클래스의")
class AnnotationHandlerAdapterTest {

    @Nested
    @DisplayName("supports 메서드는")
    class Supports {

        @Test
        @DisplayName("handler가 HandlerExecution의 구현체라면 true를 반환한다.")
        void success() {
            //given
            final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
            final HandlerExecution handler = mock(HandlerExecution.class);

            //when
            final boolean actual = handlerAdapter.supports(handler);

            //then
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("handler가 HandlerExecution의 구현체가 아니면 false를 반환한다.")
        void fail() {
            //given
            final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
            final TestManualController handler = new TestManualController();

            //when
            final boolean actual = handlerAdapter.supports(handler);

            //then
            assertThat(actual).isFalse();
        }
    }

    @Test
    @DisplayName("handle 메서드는 HandlerExecution를 handle하여 ModelAndView를 반환한다.")
    void handle() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handler = mock(HandlerExecution.class);

        final String viewName = "test";
        final ModelAndView expected = new ModelAndView(new JspView(viewName));
        given(handler.handle(request, response))
                .willReturn(expected);

        //when
        final ModelAndView actual = handlerAdapter.handle(request, response, handler);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
