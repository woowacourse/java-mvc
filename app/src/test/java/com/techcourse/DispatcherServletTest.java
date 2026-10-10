package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HandlerMappingRegistry mappings;
    private HandlerAdapterRegistry adapters;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {
        final var mapping = new AnnotationHandlerMapping("com.techcourse.controller");
        mapping.initialize();

        mappings = new HandlerMappingRegistry();
        mappings.addHandlerMapping(mapping);
        adapters = new HandlerAdapterRegistry();
        adapters.addHandlerAdapter(new HandlerExecutionAdapter());

        servlet = new DispatcherServlet(mappings, adapters);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    @DisplayName("어노테이션 컨트롤러를 실행하고 JSON 뷰를 렌더링한다")
    void dispatchesAndRendersJson() throws Exception {
        final var output = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service((ServletRequest) request, (ServletResponse) response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("매핑되지 않은 경로는 404로 응답한다")
    void returnsNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/unknown");

        servlet.service((ServletRequest) request, (ServletResponse) response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoMoreInteractions(response);
    }

    @Test
    @DisplayName("지원하는 어댑터가 없으면 원인 예외를 포함한 ServletException을 던진다")
    void throwsWhenNoAdapterSupportsHandler() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/user");
        final var handler = mappings.getHandler(request);
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        when(unsupportedAdapter.supports(handler)).thenReturn(false);
        final var unsupportedAdapters = new HandlerAdapterRegistry();
        unsupportedAdapters.addHandlerAdapter(unsupportedAdapter);
        servlet = new DispatcherServlet(mappings, unsupportedAdapters);

        assertThatThrownBy(() -> servlet.service((ServletRequest) request, (ServletResponse) response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class);

        verify(unsupportedAdapter).supports(handler);
        verify(unsupportedAdapter, never()).handle(handler, request, response);
        verifyNoInteractions(response);
    }

    @Test
    @DisplayName("앞 매핑이 null을 반환하면 다음 매핑을 조회하고 찾은 핸들러를 실행한다")
    void skipsMappingWithoutHandler() throws Exception {
        final var output = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        final var handler = mappings.getHandler(request);
        final var firstMapping = mock(HandlerMapping.class);
        final var matchingMapping = mock(HandlerMapping.class);
        final var unusedMapping = mock(HandlerMapping.class);
        when(firstMapping.getHandler(request)).thenReturn(null);
        when(matchingMapping.getHandler(request)).thenReturn(handler);
        final var orderedMappings = new HandlerMappingRegistry();
        orderedMappings.addHandlerMapping(firstMapping);
        orderedMappings.addHandlerMapping(matchingMapping);
        orderedMappings.addHandlerMapping(unusedMapping);
        servlet = new DispatcherServlet(orderedMappings, adapters);

        servlet.service((ServletRequest) request, (ServletResponse) response);

        final var order = inOrder(firstMapping, matchingMapping);
        order.verify(firstMapping).getHandler(request);
        order.verify(matchingMapping).getHandler(request);
        verifyNoInteractions(unusedMapping);
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("supports가 false인 어댑터를 건너뛰고 지원하는 어댑터로 핸들러를 실행한다")
    void skipsUnsupportedAdapter() throws Exception {
        final var output = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        final var handler = mappings.getHandler(request);
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var supportedAdapter = spy(new HandlerExecutionAdapter());
        final var unusedAdapter = mock(HandlerAdapter.class);
        when(unsupportedAdapter.supports(handler)).thenReturn(false);
        final var orderedAdapters = new HandlerAdapterRegistry();
        orderedAdapters.addHandlerAdapter(unsupportedAdapter);
        orderedAdapters.addHandlerAdapter(supportedAdapter);
        orderedAdapters.addHandlerAdapter(unusedAdapter);
        servlet = new DispatcherServlet(mappings, orderedAdapters);

        servlet.service((ServletRequest) request, (ServletResponse) response);

        final var order = inOrder(unsupportedAdapter, supportedAdapter);
        order.verify(unsupportedAdapter).supports(handler);
        order.verify(supportedAdapter).supports(handler);
        order.verify(supportedAdapter).handle(handler, request, response);
        verify(unsupportedAdapter, never()).handle(handler, request, response);
        verifyNoInteractions(unusedAdapter);
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("컨트롤러가 반환한 리다이렉트 뷰를 렌더링한다")
    void rendersRedirect() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        servlet.service((ServletRequest) request, (ServletResponse) response);

        verify(response).sendRedirect("/");
    }
}
