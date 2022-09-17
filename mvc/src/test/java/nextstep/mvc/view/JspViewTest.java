package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void redirectView를_render할_수_있다() throws ServletException, IOException {
        // given
        final JspView jspView = new JspView("redirect:/index.jsp");

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void view를_render할_수_있다() throws ServletException, IOException {
        // given
        final JspView jspView = new JspView("/index.jsp");

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        // when
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        jspView.render(Map.of(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}
