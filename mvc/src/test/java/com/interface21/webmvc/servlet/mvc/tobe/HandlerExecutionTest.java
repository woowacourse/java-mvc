package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @Test
    void invokesControllerMethodWithRequestAndResponse() throws Exception {
        final var controller = new TestController();
        final var method = TestController.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        final var execution = new HandlerExecution(controller, method);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = execution.handle(request, response);

        assertThat(modelAndView).isSameAs(controller.modelAndView);
        assertThat(controller.request).isSameAs(request);
        assertThat(controller.response).isSameAs(response);
    }

    public static class TestController {

        private final ModelAndView modelAndView = new ModelAndView(new JspView("/test.jsp"));
        private HttpServletRequest request;
        private HttpServletResponse response;

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            this.request = request;
            this.response = response;
            return modelAndView;
        }
    }
}
