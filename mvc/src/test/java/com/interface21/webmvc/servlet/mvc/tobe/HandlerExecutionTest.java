package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @Test
    void invokesControllerMethodWithRequestAndResponse() throws Exception {
        final var controller = new TestController();
        final var method = TestController.class.getDeclaredMethod(
                "handle",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        final var handlerExecution = new HandlerExecution(controller, method);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("request")).isSameAs(request);
        assertThat(modelAndView.getObject("response")).isSameAs(response);
    }

    private static class TestController {

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(mock(View.class))
                    .addObject("request", request)
                    .addObject("response", response);
        }
    }
}
