package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void 첫_화면을_JSP로_forward한다() throws Exception {
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
    void 로그아웃하면_세션을_비우고_첫_화면으로_이동한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 컨텍스트_경로가_있어도_로그아웃을_실행한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/mvc/logout");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void GET_로그아웃_요청은_세션을_변경하지_않는다() {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // when & then
        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class);
        verify(request, never()).getSession();
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
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 로그인_성공시_사용자를_세션에_저장하고_첫_화면으로_이동한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/");
    }

    @Test
    void 로그인_실패시_401_화면으로_이동한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 존재하지_않는_계정은_로그인에_실패한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("missing-user-stage3");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 이미_로그인한_사용자는_다시_로그인하지_않고_첫_화면으로_이동한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/");
        verify(session, never()).setAttribute(UserSession.SESSION_KEY, user);
    }

    @Test
    void 로그인하지_않은_사용자에게_로그인_화면을_보여준다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
    }

    @Test
    void 로그인한_사용자는_로그인_화면_대신_첫_화면으로_이동한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/");
    }

    @Test
    void 로그아웃은_POST_어노테이션으로만_매핑한다() {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // when
        Object postHandler = handlerMapping.getHandler(request);
        when(request.getMethod()).thenReturn("GET");
        Object getHandler = handlerMapping.getHandler(request);

        // then
        assertThat(postHandler).isInstanceOf(HandlerExecution.class);
        assertThat(getHandler).isNull();
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
