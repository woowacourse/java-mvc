package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

    @Test
    void supports_WhenHandlerIsHandlerExecution_ThenTrue() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(adapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void supports_WhenHandlerIsNotHandlerExecution_ThenFalse() {
        Controller controller = mock(Controller.class);
        assertThat(adapter.supports(controller)).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void handle_ThenReturnModelAndViewOfHandlerExecution() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final ModelAndView expected = new ModelAndView(new JspView("/index.jsp"));
        when(handlerExecution.handle(request, response)).thenReturn(expected);

        final ModelAndView modelAndView = adapter.handle(request, response, handlerExecution);

        assertThat(modelAndView).isSameAs(expected);
    }
}
