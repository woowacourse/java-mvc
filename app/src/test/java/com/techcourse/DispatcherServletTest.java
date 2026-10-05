package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void 컨트롤러가_JSP_뷰_이름을_반환하면_포워드한다() throws Exception {
        final var request = createRequest("GET", "/");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 애노테이션_컨트롤러로_회원가입_화면을_보여준다() throws Exception {
        final var request = createRequest("GET", "/register");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 애노테이션_컨트롤러로_회원가입한다() throws Exception {
        final var request = createRequest("POST", "/register");
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("woody");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("woody@email.com");

        dispatcherServlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount("woody")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 컨트롤러가_redirect_뷰_이름을_반환하면_리다이렉트한다() throws Exception {
        final var request = createRequest("GET", "/logout");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void 처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        final var request = createRequest("GET", "/not-found");
        final var response = mock(HttpServletResponse.class);

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private HttpServletRequest createRequest(String method, String requestURI) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn(requestURI);
        return request;
    }
}
