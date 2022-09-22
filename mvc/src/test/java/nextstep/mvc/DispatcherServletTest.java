package nextstep.mvc;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class DispatcherServletTest {

    @Test
    @DisplayName("처리할 수 없는 uri의 경우 404.jsp로 redirect 한다.")
    void notFound() throws ServletException, IOException {
        // given
        DispatcherServlet dispatcher = new DispatcherServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/test");

        // when
        dispatcher.service(request, response);

        // then
        verify(response).sendRedirect("/404.jsp");
    }

    @Test
    void render() {
        // given
        DispatcherServlet dispatcher = new DispatcherServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/login/view");

    }
}
