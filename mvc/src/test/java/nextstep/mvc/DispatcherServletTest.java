package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @DisplayName("존재하지 않은 리소스 요청이 들어오면 404 페이지로 리다이렉팅 한다.")
    @Test
    void redirect_NotFound() throws Exception {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/east");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/404.jsp");
    }
}
