package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @DisplayName("핸들러가 HandlerExecution 타입이면 true를 반환한다.")
    @Test
    void supports() {
        // given
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        final boolean actual = annotationHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("핸들러의 handle 메서드를 실행시킨다.")
    @Test
    void handle() throws Exception {
        // given
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView expected = new ModelAndView(new JspView("path"));

        given(handlerExecution.handle(request, response))
                .willReturn(expected);

        // when
        final ModelAndView modelAndView = annotationHandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
