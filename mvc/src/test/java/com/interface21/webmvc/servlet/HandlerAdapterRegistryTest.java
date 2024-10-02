package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HandlerAdapter handlerAdapter;
    private Object handler;

    @BeforeEach
    public void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handlerAdapter = mock(HandlerAdapter.class);
        handler = new Object();

        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @DisplayName("지원하는 handlerAdapter를 찾아서 ModelAndView를 반환한다.")
    @Test
    void execute_returnModelAndView() throws Exception {
        // given
        when(handlerAdapter.support(handler)).thenReturn(true);
        ModelAndView expectedModelAndView = new ModelAndView(null);
        when(handlerAdapter.handle(request, response, handler)).thenReturn(expectedModelAndView);

        // when
        ModelAndView actual = handlerAdapterRegistry.execute(request, response, handler);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(ModelAndView.class);
        verify(handlerAdapter).handle(request, response, handler);
    }

    @DisplayName("지원하는 handlerAdapter를 찾지 못하면, 예외가 발생한다.")
    @Test
    void execute_returnEmptyOptional() throws Exception {
        // given
        when(handlerAdapter.support(handler)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.execute(request, response, handler))
                .isInstanceOf(NoSuchElementException.class);
        verify(handlerAdapter, never()).handle(any(), any(), any());
    }
}
