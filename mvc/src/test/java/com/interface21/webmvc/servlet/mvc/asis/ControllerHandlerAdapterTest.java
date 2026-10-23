package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void supportsController() {
        assertThat(handlerAdapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void wrapViewNameWithJspView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final var modelAndView = handlerAdapter.handle(request, response, new ForwardController("/index.jsp"));
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        verify(dispatcher).forward(request, response);
    }
}
