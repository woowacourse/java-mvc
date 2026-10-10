package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class HandlerAdapterTest {

    @Test
    void adapterSupportsOnlyHandlerExecution() throws Exception {
        final var target = new ProfileController(new ModelAndView(mock(View.class)));
        final var execution = new HandlerExecution(target, ProfileController.class.getMethod(
                "profile", HttpServletRequest.class, HttpServletResponse.class));
        final HandlerAdapter executionAdapter = new HandlerExecutionAdapter();

        assertThat(executionAdapter.supports(execution)).isTrue();
        assertThat(executionAdapter.supports(new Object())).isFalse();
        assertThat(executionAdapter.supports(null)).isFalse();
    }

    @Test
    void invokesAnnotatedHandlerAndPreservesItsModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        final var view = mock(View.class);
        final var expected = new ModelAndView(view);
        final var target = new ProfileController(expected);
        final var execution = new HandlerExecution(target, ProfileController.class.getMethod(
                "profile", HttpServletRequest.class, HttpServletResponse.class));
        final HandlerAdapter adapter = new HandlerExecutionAdapter();

        final var result = adapter.handle(request, response, execution);

        assertThat(result).isSameAs(expected);
        assertThat(result.getObject("id")).isEqualTo("gugu");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verifyNoInteractions(view);

        result.getView().render(result.getModel(), request, response);

        verify(view).render(result.getModel(), request, response);
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
