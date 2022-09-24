package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("HandlerAdapter가 ManualHandler이면 False를 반환한다.")
    void typeOfHandlerExecutionIsExecutedManualHandler() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

        assertThat(handlerAdapter.supports(handlerExecution)).isFalse();
    }

    @Test
    @DisplayName("HandlerAdapter가 AnnotationHandlerAdapter이면 True 반환한다.")
    void typeOfHandlerExecutionIsExecutedAnnotationHandler() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
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
