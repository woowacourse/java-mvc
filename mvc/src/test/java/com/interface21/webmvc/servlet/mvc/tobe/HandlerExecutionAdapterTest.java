package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionAdapterTest {

    @Test
    @DisplayName("HandlerExecution 타입만 지원한다")
    void supports() {
        final var adapter = new HandlerExecutionAdapter();

        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    @DisplayName("HandlerExecution을 실행하고 ModelAndView를 반환한다")
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerExecution = mock(HandlerExecution.class);
        final var expected = new ModelAndView(mock(View.class));
        when(handlerExecution.handle(request, response)).thenReturn(expected);

        final var adapter = new HandlerExecutionAdapter();

        assertThat(adapter.handle(handlerExecution, request, response)).isSameAs(expected);
    }
}
