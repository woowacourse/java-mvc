package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HandlerExecutionAdapterTest {

    private final HandlerAdapter adapter = new HandlerExecutionAdapter();

    @Test
    void supportsOnlyHandlerExecutions() {
        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(mock(Controller.class))).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void returnsOriginalModelAndViewWithRequestAndResponsePassedToHandler() throws Exception {
        final var handler = mock(HandlerExecution.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        final var expected = new ModelAndView(view).addObject("id", "gugu");
        when(handler.handle(request, response)).thenReturn(expected);

        final var result = adapter.handle(handler, request, response);

        assertThat(result).isSameAs(expected);
        verify(handler).handle(request, response);
        verifyNoInteractions(view);
    }
}
