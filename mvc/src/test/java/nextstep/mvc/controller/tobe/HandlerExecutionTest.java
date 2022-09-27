package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    @Test
    void HandlerExecution_은_생성된_메서드를_실행하여_ModelAndView_를_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response))
                .thenReturn(new ModelAndView(new JspView("/test.jsp")));

        ModelAndView modelAndView = handlerExecution.handle(request, response);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
