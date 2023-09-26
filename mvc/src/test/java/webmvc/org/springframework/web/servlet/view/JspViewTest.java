package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JspViewTest {

    @DisplayName("JspView는 Model을 Jsp로 변환한다.")
    @Test
    void render() throws ServletException, IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Map<String, ?> model = new HashMap<>(Map.of());

        JspView jspView = new JspView("/index.jsp");
        given(request.getRequestDispatcher("/index.jsp")).willReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).getRequestDispatcher("/index.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("JspView는 redirect:/로 시작하는 경우 redirect를 수행한다.")
    @Test
    void render_redirect() throws ServletException, IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Map<String, ?> model = new HashMap<>(Map.of());

        JspView jspView = new JspView("redirect:/index.jsp");

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
    }
}
