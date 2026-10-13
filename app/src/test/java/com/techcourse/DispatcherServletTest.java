package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        response = mock(HttpServletResponse.class);
    }

    @Test
    void 레거시_컨트롤러가_처리한_요청을_forward한다() throws Exception {
        final var request = request("GET", "/");
        final var requestDispatcher = requestDispatcher(request, "/index.jsp");

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 레거시_매핑에_없는_요청은_어노테이션_컨트롤러가_처리한다() throws Exception {
        final var request = request("GET", "/register");
        final var requestDispatcher = requestDispatcher(request, "/register.jsp");

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 같은_URL이라도_HTTP_메서드에_맞는_어노테이션_핸들러가_처리한다() throws Exception {
        final var request = request("POST", "/register");
        when(request.getParameter("account")).thenReturn("dispatcher-test");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("dispatcher-test@example.com");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount("dispatcher-test")).isPresent();
    }

    @Test
    void 어느_매핑에도_없는_요청이면_예외가_발생한다() {
        final var request = request("GET", "/not-found");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("GET /not-found");
    }

    private HttpServletRequest request(final String method, final String uri) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getContextPath()).thenReturn("");
        return request;
    }

    private RequestDispatcher requestDispatcher(final HttpServletRequest request, final String path) {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        return requestDispatcher;
    }
}
