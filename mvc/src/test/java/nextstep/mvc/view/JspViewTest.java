package nextstep.mvc.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void render() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);

        JspView jspView = new JspView("index.jsp");
        jspView.render(Map.of("account", "gugu"), request, response);

        verify(requestDispatcher, times(1)).forward(any(), any());
    }

    @Test
    void render_redirect_view() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        JspView jspView = new JspView("redirect:/index.jsp");
        jspView.render(Map.of("account", "gugu"), request, response);

        verify(response, times(1)).sendRedirect(any());
    }
}
