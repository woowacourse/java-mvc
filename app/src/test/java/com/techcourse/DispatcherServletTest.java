package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void 주입된_핸들러_매핑을_순서대로_초기화한다() {
        // given
        HandlerMapping first = mock(HandlerMapping.class);
        HandlerMapping second = mock(HandlerMapping.class);
        DispatcherServlet servlet = new DispatcherServlet(List.of(first, second));

        // when
        servlet.init();

        // then
        InOrder initializationOrder = inOrder(first, second);
        initializationOrder.verify(first).initialize();
        initializationOrder.verify(second).initialize();
    }

    @Test
    void 기존_컨트롤러의_JSP_응답을_forward한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 기존_컨트롤러의_redirect_응답을_유지한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 컨텍스트_경로가_있어도_기존_컨트롤러를_실행한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/mvc/logout");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void 같은_주소가_겹치면_어노테이션_매핑을_먼저_사용한다() throws Exception {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        ManualHandlerMapping manualHandlerMapping = mock(ManualHandlerMapping.class);
        DispatcherServlet servlet = new DispatcherServlet(List.of(annotationHandlerMapping, manualHandlerMapping));
        servlet.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HandlerExecution execution = mock(HandlerExecution.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(dispatcher);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(execution);
        when(execution.handle(request, response)).thenReturn(new ModelAndView(new JspView("/annotation.jsp")));

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(manualHandlerMapping, never()).getHandler(request);
    }

    @Test
    void 가입_화면을_어노테이션_컨트롤러로_표시한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 가입_요청을_어노테이션_컨트롤러로_저장한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String account = "junie-stage2-test";
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("junie@example.com");

        // when
        servlet.service(request, response);

        // then
        User savedUser = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(savedUser.getAccount()).isEqualTo(account);
        assertThat(savedUser.checkPassword("secret")).isTrue();
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 사용자_조회_요청을_JSON으로_응답한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseBody = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        servlet.service(request, response);

        // then
        assertThat(responseBody.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
