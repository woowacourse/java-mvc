package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    @Test
    void support() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

        assertThat(adapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ModelAndView modelAndView = mock(ModelAndView.class);
        when(handlerExecution.handle(request, response)).thenReturn(modelAndView);

        HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

        assertThat(adapter.handle(request, response, handlerExecution)).isInstanceOf(ModelAndView.class);
    }

}
