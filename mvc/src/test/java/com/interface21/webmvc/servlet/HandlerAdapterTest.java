package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class HandlerAdapterTest {

    @Test
    void adaptersSupportOnlyTheirHandlerType() throws Exception {
        final var controller = mock(Controller.class);
        final var target = new ProfileController(new ModelAndView(mock(View.class)));
        final var execution = new HandlerExecution(target, ProfileController.class.getMethod(
                "profile", HttpServletRequest.class, HttpServletResponse.class));
        final HandlerAdapter controllerAdapter = new ControllerHandlerAdapter();
        final HandlerAdapter executionAdapter = new HandlerExecutionAdapter();

        assertThat(controllerAdapter.supports(controller)).isTrue();
        assertThat(controllerAdapter.supports(execution)).isFalse();
        assertThat(executionAdapter.supports(controller)).isFalse();
        assertThat(executionAdapter.supports(execution)).isTrue();
        assertThat(controllerAdapter.supports(new Object())).isFalse();
        assertThat(executionAdapter.supports(new Object())).isFalse();
        assertThat(controllerAdapter.supports(null)).isFalse();
        assertThat(executionAdapter.supports(null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/profile.jsp", "redirect:/profile.jsp"})
    void preservesLegacyForwardAndRedirectAfterAdapting(final String viewName) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var controller = mock(Controller.class);
        when(controller.execute(request, response)).thenReturn(viewName);
        when(request.getRequestDispatcher("/profile.jsp")).thenReturn(dispatcher);
        final HandlerAdapter adapter = new ControllerHandlerAdapter();

        final var result = adapter.handle(request, response, controller);

        verify(controller).execute(request, response);
        verifyNoInteractions(response, dispatcher);

        result.getView().render(result.getModel(), request, response);

        if (viewName.startsWith("redirect:")) {
            verify(response).sendRedirect("/profile.jsp");
            verifyNoInteractions(dispatcher);
        } else {
            verify(dispatcher).forward(request, response);
            verify(response, never()).sendRedirect(anyString());
        }
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
