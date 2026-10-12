package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HandlerMapping mapping;
    private HandlerAdapter adapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        mapping = mock(HandlerMapping.class);
        adapter = mock(HandlerAdapter.class);
        servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("핸들러를 찾고 지원하는 어댑터로 실행한 결과의 View를 렌더링한다")
    void findsHandlerAndSupportingAdapterThenRendersView() throws Exception {
        HandlerMapping unmatchedMapping = mock(HandlerMapping.class);
        HandlerAdapter unsupportedAdapter = mock(HandlerAdapter.class);
        servlet = new DispatcherServlet(
                List.of(unmatchedMapping, mapping),
                List.of(unsupportedAdapter, adapter)
        );
        Object handler = new Object();
        View view = mock(View.class);
        ModelAndView modelAndView = new ModelAndView(view).addObject("message", "hello");
        when(mapping.getHandler(request)).thenReturn(handler);
        when(adapter.supports(handler)).thenReturn(true);
        when(adapter.handle(handler, request, response)).thenReturn(modelAndView);

        servlet.service(request, response);

        verify(adapter).handle(handler, request, response);
        verify(view).render(modelAndView.getModel(), request, response);
    }

    @Test
    @DisplayName("요청에 해당하는 핸들러가 없으면 404로 응답한다")
    void returnsNotFoundWhenNoHandlerMatches() throws Exception {
        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(adapter);
    }

    @Test
    @DisplayName("핸들러를 지원하는 어댑터가 없으면 ServletException을 전달한다")
    void throwsWhenNoAdapterSupportsHandler() {
        when(mapping.getHandler(request)).thenReturn(new Object());

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }
}
