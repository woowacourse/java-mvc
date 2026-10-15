package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HandlerMapping mapping = mock(HandlerMapping.class);
    private final HandlerAdapter adapter = mock(HandlerAdapter.class);

    @Test
    void delegatesToMatchingMappingAndSupportingAdapterThenRendersView() throws Exception {
        final var emptyMapping = mock(HandlerMapping.class);
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var handler = new Object();
        final var view = mock(View.class);
        final var modelAndView = new ModelAndView(view).addObject("id", 1);
        when(mapping.getHandler(request)).thenReturn(handler);
        when(adapter.supports(handler)).thenReturn(true);
        when(adapter.handle(request, response, handler)).thenReturn(modelAndView);
        final var servlet = new DispatcherServlet(List.of(emptyMapping, mapping), List.of(unsupportedAdapter, adapter));

        servlet.service(request, response);

        verify(adapter).handle(request, response, handler);
        verify(view).render(modelAndView.getModel(), request, response);
    }

    @Test
    void returnsNotFoundWhenNoMappingMatches() throws Exception {
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(adapter);
    }

    @Test
    void failsWhenNoAdapterSupportsHandler() {
        when(mapping.getHandler(request)).thenReturn(new Object());
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }
}
