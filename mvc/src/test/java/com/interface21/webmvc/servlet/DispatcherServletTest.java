package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    @DisplayName("매핑과 어댑터를 순서대로 찾아 실행하고 모델을 뷰에 전달한다")
    void dispatchesThroughMatchingMappingAndAdapter() throws Exception {
        final var unmatchedMapping = mock(HandlerMapping.class);
        final var matchedMapping = mock(HandlerMapping.class);
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var supportedAdapter = mock(HandlerAdapter.class);
        final var handler = new Object();
        final var view = mock(View.class);
        final var modelAndView = new ModelAndView(view).addObject("name", "재키");
        when(matchedMapping.getHandler(request)).thenReturn(handler);
        when(supportedAdapter.supports(handler)).thenReturn(true);
        when(supportedAdapter.handle(request, response, handler)).thenReturn(modelAndView);
        final var servlet = new DispatcherServlet(
                List.of(unmatchedMapping, matchedMapping), List.of(unsupportedAdapter, supportedAdapter));

        servlet.service(request, response);

        verify(unmatchedMapping).getHandler(request);
        verify(view).render(Map.of("name", "재키"), request, response);
    }

    @Test
    @DisplayName("처리할 핸들러가 없으면 어댑터를 실행하지 않고 404로 응답한다")
    void respondsWithNotFoundWithoutInvokingAdapter() throws Exception {
        final var adapter = mock(HandlerAdapter.class);
        final var servlet = new DispatcherServlet(List.of(mock(HandlerMapping.class)), List.of(adapter));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(adapter);
    }

    @Test
    @DisplayName("핸들러를 지원하는 어댑터가 없으면 실패 원인을 보존한다")
    void failsWhenNoAdapterSupportsHandler() {
        final var mapping = mock(HandlerMapping.class);
        when(mapping.getHandler(request)).thenReturn(new Object());
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(mock(HandlerAdapter.class)));

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("지원하는 HandlerAdapter가 없습니다: java.lang.Object");
    }
}
