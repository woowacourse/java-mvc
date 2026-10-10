package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionAdapterTest {

    private final HandlerAdapter adapter = new HandlerExecutionAdapter();

    @Test
    void supportsOnlyHandlerExecutions() {
        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(mock(Controller.class))).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void executesHandlerAndPreservesModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var controller = new SampleController();
        final var method = SampleController.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(controller, method);

        final var result = adapter.handle(request, response, handler);

        assertThat(result).isSameAs(controller.modelAndView);
        assertThat(result.getObject("request")).isSameAs(request);
        assertThat(result.getObject("response")).isSameAs(response);
    }

    public static class SampleController {

        private final ModelAndView modelAndView = new ModelAndView(new JspView("/index.jsp"));

        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
            return modelAndView.addObject("request", request).addObject("response", response);
        }
    }
}
