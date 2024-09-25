package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdaptersTest {

    @DisplayName("handle 메서드가 핸들러를 지원하는 경우 ModelAndView 반환")
    @Test
    void adapter_handle() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerAdapter handlerAdapter = mock(HandlerAdapter.class);
        Object handler = new Object();
        ModelAndView expectedmodelAndView = mock(ModelAndView.class);
        HandlerAdapters handlerAdapters = new HandlerAdapters(List.of(handlerAdapter));

        //when
        when(handlerAdapter.isSupport(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler)).thenReturn(expectedmodelAndView);
        ModelAndView actualModelAndView = handlerAdapters.handle(request, response, handler);

        //then
        assertThat(actualModelAndView).isEqualTo(expectedmodelAndView);
    }

    @DisplayName("handle 메서드가 핸들러를 지원하지 않는 경우 예외")
    @Test
    void adapter_notFound() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();
        HandlerAdapters handlerAdapters = new HandlerAdapters(List.of());

        //when & then
        assertThatThrownBy(() -> handlerAdapters.handle(request, response, handler))
                .isInstanceOf(ServletException.class)
                .hasMessage("No handler found for requestURI: " + request.getRequestURI());
    }
}