package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("Handler의 반환타입이 HandlerExecution이면 ManualHandler를 실행한다.")
    void typeOfHandlerExecutionIsExecutedManualHandler() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerAdapter = new ManualHandlerAdapter();

        assertThat(handlerAdapter.supports(handlerExecution));
    }

    @Test
    @DisplayName("Handler의 반환타입이 HandlerExecution이면 AnnotationHandler를 실행한다.")
    void typeOfHandlerExecutionIsExecutedAnnotationHandler() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        assertThat(handlerAdapter.supports(handlerExecution));
    }

    @Test
    @DisplayName("AnnotationHandler는 ModelAndView에서 JspView를 반환할 수 있다.")
    void typeOfAnnotationHandlerIsReturnsModelAndView() throws Exception {
        // given
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response))
            .thenReturn(new ModelAndView(new JspView(null)));

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
