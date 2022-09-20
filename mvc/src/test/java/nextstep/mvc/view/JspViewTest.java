package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

    @DisplayName("forward 요청을 렌더링한다")
    @Test
    void renderForward() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final JspView jspView = new JspView("/index.jsp");
        final Map<String, ?> model = Map.of("name", "kth990303");
        jspView.render(model, request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute("name", "kth990303");
    }

    @DisplayName("redirect 요청을 렌더링한다")
    @Test
    void renderRedirect() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final JspView jspView = new JspView("redirect:/index.jsp");
        final Map<String, ?> model = Map.of("name", "kth990303");
        jspView.render(model, request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request).setAttribute("name", "kth990303");
    }
}