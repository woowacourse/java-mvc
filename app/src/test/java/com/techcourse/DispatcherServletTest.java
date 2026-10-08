package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private TestDispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new TestDispatcherServlet();
        dispatcherServlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void 루트_요청을_어노테이션_컨트롤러로_처리한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.dispatch(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 회원가입_화면을_어노테이션_컨트롤러로_처리한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.dispatch(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 사용자_API를_JSON으로_응답한다() throws Exception {
        StringWriter output = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        dispatcherServlet.dispatch(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void 사용자_API에_account가_없으면_400으로_응답한다() throws Exception {
        StringWriter output = responseWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.dispatch(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertThat(output.toString()).isEqualTo("{\"message\":\"account parameter is required\"}");
    }

    @Test
    void 존재하지_않는_사용자를_조회하면_404로_응답한다() throws Exception {
        StringWriter output = responseWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("unknown");

        dispatcherServlet.dispatch(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(output.toString()).isEqualTo("{\"message\":\"user not found\"}");
    }

    private StringWriter responseWriter() throws Exception {
        StringWriter output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        return output;
    }

    private static class TestDispatcherServlet extends DispatcherServlet {

        private TestDispatcherServlet() {
            super("com.techcourse.controller");
        }

        private void dispatch(
                HttpServletRequest request,
                HttpServletResponse response
        ) throws ServletException {
            super.service(request, response);
        }
    }
}
