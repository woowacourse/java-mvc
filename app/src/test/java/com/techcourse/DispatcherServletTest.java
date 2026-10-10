package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {
        final var mapping = new AnnotationHandlerMapping("com.techcourse.controller");
        mapping.initialize();

        final var mappings = new HandlerMappingRegistry();
        mappings.addHandlerMapping(mapping);
        final var adapters = new HandlerAdapterRegistry();
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
