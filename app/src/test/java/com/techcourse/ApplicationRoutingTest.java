package com.techcourse;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationRoutingTest {

    private Servlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        final ServletContext servletContext = mock(ServletContext.class);
        final ServletRegistration.Dynamic registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        final ArgumentCaptor<Servlet> servletCaptor = ArgumentCaptor.forClass(Servlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), servletCaptor.capture());
        servlet = servletCaptor.getValue();
        final ServletConfig servletConfig = mock(ServletConfig.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        servlet.init(servletConfig);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
    }

    @Test
    void 로그인하지_않았으면_로그인_화면을_보여준다() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 어노테이션_컨트롤러로_회원가입_화면을_보여준다() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(request).getRequestDispatcher("/register.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 회원가입_화면_경로로_요청하면_회원가입_화면을_보여준다() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(request).getRequestDispatcher("/register.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 어노테이션_컨트롤러로_회원가입하고_메인으로_리다이렉트한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("yorke");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("yorke@woowahan.com");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount("yorke")).isPresent();
    }

    @Test
    void 처리할_컨트롤러가_없으면_404를_응답한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/not-found");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 루트로_요청하면_메인_화면을_보여준다() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(request).getRequestDispatcher("/index.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 이미_로그인했으면_로그인_화면_대신_메인으로_리다이렉트한다() throws Exception {
        final HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 올바른_계정과_비밀번호로_로그인하면_세션에_저장하고_메인으로_리다이렉트한다() throws Exception {
        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        verify(session).setAttribute(eq(UserSession.SESSION_KEY), any(User.class));
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 비밀번호가_틀리면_401_화면으로_리다이렉트한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 없는_계정으로_로그인하면_401_화면으로_리다이렉트한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("nobody");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 로그아웃하면_세션에서_사용자를_지우고_루트로_리다이렉트한다() throws Exception {
        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void 사용자_조회_API는_JSON으로_응답한다() throws Exception {
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body, true));
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");

        servlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).contains("\"account\":\"gugu\"");
    }
}
