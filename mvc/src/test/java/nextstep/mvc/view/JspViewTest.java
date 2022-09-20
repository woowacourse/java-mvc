package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void redirect_접두사가_있으면_redirect_한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final JspView jspView = new JspView("redirect:/");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void redirect_접두사가_없으면_forward_한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final JspView jspView = new JspView("/index.jsp");

        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        jspView.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
