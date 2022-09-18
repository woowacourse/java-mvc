package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("forward 요청을 렌더링한다.")
    void render() throws Exception {
        final JspView jspView = new JspView("/index.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        jspView.render(Collections.emptyMap(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("redirect 요청일 경우 sendRedirect를 호출하여 처리한다.")
    void renderWithRedirect() throws Exception {
        final JspView jspView = new JspView("redirect:/index.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        jspView.render(Collections.emptyMap(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
