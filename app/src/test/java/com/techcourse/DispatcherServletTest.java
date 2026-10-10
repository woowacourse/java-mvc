package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
    }

    @DisplayName("init()은 등록된 모든 HandlerMapping을 초기화한다")
    @Test
    void initializesAllMappings() {
        final var first = mock(HandlerMapping.class);
        final var second = mock(HandlerMapping.class);

        new DispatcherServlet(List.of(first, second), List.of()).init();

        verify(first).initialize();
        verify(second).initialize();
    }

    @DisplayName("여러 매핑이 핸들러를 가지고 있으면 먼저 등록된 매핑의 핸들러를 사용한다")
    @Test
    void usesFirstMappingByPriority() throws Exception {
        final var firstHandler = new Object();
        final var secondHandler = new Object();
        final var adapter = alwaysSupportingAdapter(new ModelAndView(mock(View.class)));

        new DispatcherServlet(
                List.of(mappingReturning(firstHandler), mappingReturning(secondHandler)),
                List.of(adapter)
        ).service(request, response);

        verify(adapter).handle(request, response, firstHandler);
        verify(adapter, never()).handle(request, response, secondHandler);
    }

    @DisplayName("앞선 매핑이 null을 반환하면 다음 매핑에게 넘긴다")
    @Test
    void skipsMappingReturningNull() throws Exception {
        final var handler = new Object();
        final var adapter = alwaysSupportingAdapter(new ModelAndView(mock(View.class)));

        new DispatcherServlet(
                List.of(mappingReturning(null), mappingReturning(handler)),
                List.of(adapter)
        ).service(request, response);

        verify(adapter).handle(request, response, handler);
    }

    @DisplayName("어댑터가 반환한 ModelAndView의 View로 렌더링한다")
    @Test
    void rendersWithReturnedView() throws Exception {
        final var view = mock(View.class);
        final var modelAndView = new ModelAndView(view);

        new DispatcherServlet(
                List.of(mappingReturning(new Object())),
                List.of(alwaysSupportingAdapter(modelAndView))
        ).service(request, response);

        verify(view).render(modelAndView.getModel(), request, response);
    }

    @DisplayName("어떤 매핑도 핸들러를 찾지 못하면 404를 응답한다")
    @Test
    void respondsNotFoundWhenNoHandler() throws Exception {
        final var adapter = mock(HandlerAdapter.class);

        new DispatcherServlet(List.of(mappingReturning(null)), List.of(adapter))
                .service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(adapter, never()).handle(any(), any(), any());
    }

    @DisplayName("핸들러를 실행할 어댑터가 없으면 원인을 담은 예외가 발생한다")
    @Test
    void failsWhenNoAdapterSupportsHandler() {
        final var adapter = mock(HandlerAdapter.class);
        when(adapter.supports(any())).thenReturn(false);
        final var servlet = new DispatcherServlet(List.of(mappingReturning(new Object())), List.of(adapter));

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    private HandlerMapping mappingReturning(final Object handler) {
        final var mapping = mock(HandlerMapping.class);
        when(mapping.getHandler(any())).thenReturn(handler);
        return mapping;
    }

    private HandlerAdapter alwaysSupportingAdapter(final ModelAndView modelAndView) throws Exception {
        final var adapter = mock(HandlerAdapter.class);
        when(adapter.supports(any())).thenReturn(true);
        when(adapter.handle(any(), any(), any())).thenReturn(modelAndView);
        return adapter;
    }
}