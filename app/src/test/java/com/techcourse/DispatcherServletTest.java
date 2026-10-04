package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.asis.SimpleControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        final var handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));

        final var handlerAdaptorRegistry = new HandlerAdaptorRegistry();
        handlerAdaptorRegistry.addHandlerAdaptor(new SimpleControllerHandlerAdaptor());
        handlerAdaptorRegistry.addHandlerAdaptor(new AnnotationHandlerAdaptor());

        servlet = new DispatcherServlet(handlerMappingRegistry, handlerAdaptorRegistry);
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    @DisplayName("홈 요청을 처리한 뒤 index.jsp로 포워드한다")
    void forward() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("애노테이션 기반 회원가입 화면 요청을 register.jsp로 포워드한다")
    void annotationHandlerForward() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("로그아웃 요청을 처리한 뒤 홈으로 리다이렉트한다")
    void redirect() throws Exception {
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("뷰 처리 중 발생한 예외를 ServletException으로 전달한다")
    void renderFailure() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        doThrow(new IOException("포워드 실패")).when(dispatcher).forward(request, response);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("포워드 실패");
    }
}
