package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DispatcherServletTest {

    @Test
    @DisplayName("어노테이션 핸들러가 반환한 뷰에 모델을 전달한다")
    void render_annotation_handler_model() throws Exception {
        final var servlet = createServletWithAnnotationMapping();
        final var request = request("/login/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        when(request.getAttribute("view")).thenReturn(view);

        servlet.service(request, response);

        verify(view).render(Map.of("id", "gugu"), request, response);
    }

    @Test
    @DisplayName("등록된 매핑에서 처음으로 일치하는 핸들러를 선택한다")
    void choose_first_matching_mapping() throws Exception {
        final var unmatchedMapping = mock(HandlerMapping.class);
        final var mapping = mock(HandlerMapping.class);
        final var laterMapping = mock(HandlerMapping.class);
        final var handler = new Object();
        final var adapter = mock(HandlerAdapter.class);
        final var servlet = new DispatcherServlet(List.of(unmatchedMapping, mapping, laterMapping), List.of(adapter));
        final var request = request("/test", "GET");
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        when(mapping.getHandler(request)).thenReturn(handler);
        when(adapter.supports(handler)).thenReturn(true);
        when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(view));

        servlet.service(request, response);

        verify(unmatchedMapping).getHandler(request);
        verify(view).render(Map.of(), request, response);
        verifyNoInteractions(laterMapping);
    }

    @Test
    @DisplayName("핸들러가 없으면 실행 없이 404로 응답한다")
    void respond_not_found_without_handler() throws Exception {
        final var adapter = mock(HandlerAdapter.class);
        final HandlerMapping mapping = request -> null;
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));
        final var request = request("/missing", "GET");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(adapter);
    }

    @Test
    @DisplayName("핸들러를 지원하는 첫 번째 어댑터로 실행한다")
    void choose_first_supporting_adapter() throws Exception {
        final var handler = new Object();
        final HandlerMapping mapping = request -> handler;
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var adapter = mock(HandlerAdapter.class);
        final var laterAdapter = mock(HandlerAdapter.class);
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(unsupportedAdapter, adapter, laterAdapter));
        final var request = request("/test", "GET");
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        final var modelAndView = new ModelAndView(view).addObject("id", "gugu");
        when(adapter.supports(handler)).thenReturn(true);
        when(adapter.handle(request, response, handler)).thenReturn(modelAndView);

        servlet.service(request, response);

        verify(unsupportedAdapter, never()).handle(request, response, handler);
        verify(view).render(Map.of("id", "gugu"), request, response);
        verifyNoInteractions(laterAdapter);
    }

    @Test
    @DisplayName("어댑터를 찾지 못하면 원인을 포함한 ServletException으로 전달한다")
    void preserve_missing_adapter_cause() {
        final var handler = new Object();
        final HandlerMapping mapping = request -> handler;
        final var servlet = new DispatcherServlet(List.of(mapping), List.of());

        assertThatThrownBy(() -> servlet.service(request("/test", "GET"), mock(HttpServletResponse.class)))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No adapter supports handler");
    }

    @Test
    @DisplayName("핸들러 실행 실패의 원인을 보존한다")
    void preserve_execution_failure_cause() throws Exception {
        final var handler = new Object();
        final HandlerMapping mapping = request -> handler;
        final var adapter = mock(HandlerAdapter.class);
        final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));
        final var request = request("/test", "GET");
        final var response = mock(HttpServletResponse.class);
        final var failure = new IllegalArgumentException("execution failed");
        when(adapter.supports(handler)).thenReturn(true);
        when(adapter.handle(request, response, handler)).thenThrow(failure);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }

    @Test
    @DisplayName("뷰 렌더링 실패의 원인을 보존한다")
    void preserve_rendering_failure_cause() throws Exception {
        final var servlet = createServletWithAnnotationMapping();
        final var request = request("/login/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        final var failure = new IllegalStateException("render failed");
        when(request.getAttribute("view")).thenReturn(view);
        doThrow(failure).when(view).render(Map.of("id", "gugu"), request, response);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }

    private DispatcherServlet createServletWithAnnotationMapping() {
        final var annotationMapping = new AnnotationHandlerMapping("dispatcher.samples");
        annotationMapping.initialize();
        return new DispatcherServlet(List.of(annotationMapping), List.of(new HandlerExecutionHandlerAdapter()));
    }

    private HttpServletRequest request(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
