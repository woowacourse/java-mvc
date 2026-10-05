package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    @Test
    void invokesControllerWithRequestAndResponseAndReturnsItsResult() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var expected = new ModelAndView(mock(View.class));
        final var controller = new ProfileController(expected);
        final var method = ProfileController.class.getMethod(
                "profile", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(controller, method);

        final var result = handlerExecution.handle(request, response);

        assertThat(result).isSameAs(expected);
        assertThat(result.getObject("id")).isEqualTo("gugu");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    public static class ProfileController {

        private final ModelAndView result;

        public ProfileController(final ModelAndView result) {
            this.result = result;
        }

        public ModelAndView profile(final HttpServletRequest request, final HttpServletResponse response) {
            result.addObject("id", request.getAttribute("id"));
            response.setStatus(HttpServletResponse.SC_OK);
            return result;
        }
    }
}
