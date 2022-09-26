package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("viewName에 따라 적절한 resource에 forward 한다.")
    @Test
    void render_forward() throws Exception {
        final JspView jspView = new JspView("/index.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        jspView.render(Map.of(), request, response);

        verify(request)
                .getRequestDispatcher("/index.jsp");
        verify(requestDispatcher)
                .forward(request, response);
    }

    @DisplayName("viewName이 redirect:로 시작하면 redirect 처리한다.")
    @Test
    void render_redirect() throws Exception {
        final JspView jspView = new JspView("redirect:/index.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        jspView.render(null, request, response);

        verify(response)
                .sendRedirect("/index.jsp");
    }
}
