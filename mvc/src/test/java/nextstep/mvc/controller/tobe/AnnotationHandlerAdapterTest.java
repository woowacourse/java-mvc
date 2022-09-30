package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

class AnnotationHandlerAdapterTest {

    @DisplayName("처리할 수 있는 핸들러일 경우 True를 반환한다.")
    @Test
    void isSupportedHandler() {
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        boolean expected = handlerAdapter.supports(handlerExecution);
        assertThat(expected).isTrue();
    }

    @DisplayName("handle메서드를 통해 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        ModelAndView actual = new ModelAndView(new JspView("result"));
        when(handlerExecution.handle(request, response)).thenReturn(actual);

        final ModelAndView expected = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(expected).isEqualTo(actual);
    }
}
