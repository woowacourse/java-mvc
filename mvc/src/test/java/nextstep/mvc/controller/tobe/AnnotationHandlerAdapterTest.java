package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution 객체가 맞다면 true를 반환한다.")
    @Test
    void supports_returnsTrue_ifHandlerExecution() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        final boolean expected = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(expected).isTrue();
    }

    @DisplayName("Handler 객체가 아니라면 false를 반환한다.")
    @Test
    void supports_returnsFalse_ifNotHandlerExecution() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final Controller controller = mock(Controller.class);

        // when
        final boolean expected = handlerAdapter.supports(controller);

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void handle() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final ModelAndView actual = new ModelAndView(new JspView("name"));
        when(handlerExecution.handle(request, response)).thenReturn(actual);

        // when
        final ModelAndView expected = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(expected).isEqualTo(actual);

    }
}
