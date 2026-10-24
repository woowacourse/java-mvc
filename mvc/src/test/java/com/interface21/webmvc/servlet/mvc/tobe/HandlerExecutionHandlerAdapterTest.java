package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

    @Test
    void supportsOnlyHandlerExecutions() throws Exception {
        assertThat(adapter.supports(execution())).isTrue();
        assertThat(adapter.supports(new ForwardController("/index.jsp"))).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void preservesModelAndViewAndPassesRequestAndResponse() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getParameter("name")).thenReturn("gugu");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(dispatcher);

        final var modelAndView = adapter.handle(request, response, execution());
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(modelAndView.getObject("name")).isEqualTo("gugu");
        verify(request).setAttribute("name", "gugu");
        verify(dispatcher).forward(request, response);
        verify(response).setStatus(201);
    }

    private HandlerExecution execution() throws Exception {
        return new HandlerExecution(new TestController(),
                TestController.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class));
    }

    public static class TestController {

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            response.setStatus(201);
            return new ModelAndView(new JspView("/annotation.jsp")).addObject("name", request.getParameter("name"));
        }
    }
}
