package com.techcourse;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

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
    void mvc_모듈의_DispatcherServlet을_등록한다() {
        assertThat(servlet).isInstanceOf(DispatcherServlet.class);
    }

    @Test
    void 레거시_컨트롤러로_로그인_화면을_보여준다() throws Exception {
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
}
