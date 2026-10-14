package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

    @DisplayName("HandlerExecution만 지원한다")
    @Test
    void supportsOnlyHandlerExecution() {
        final Controller legacyController = (request, response) -> "/index.jsp";

        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(legacyController)).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @DisplayName("HandlerExecution이 반환한 ModelAndView를 변환 없이 그대로 전달한다")
    @Test
    void passesModelAndViewThrough() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var execution = mock(HandlerExecution.class);
        final var expected = new ModelAndView(mock(View.class));
        when(execution.handle(request, response)).thenReturn(expected);

        final var actual = adapter.handle(request, response, execution);

        assertThat(actual).isSameAs(expected);
    }
}