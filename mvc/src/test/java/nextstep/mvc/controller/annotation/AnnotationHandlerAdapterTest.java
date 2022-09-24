package nextstep.mvc.controller.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @Test
    void 핸들러의_반환타입이_HandlerExecution_인_경우_AnnotationHandlerAdapter_가_실행된다() {
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void AnnotationHandlerAdapter_는_실행할_메서드의_ModelAndView_를_반환한다() throws Exception {
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response)).thenReturn(new ModelAndView(new JspView("")));

        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
