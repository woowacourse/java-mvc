package nextstep.mvc.view;

import static nextstep.mvc.view.JspView.REDIRECT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JspViewTest {

    @Test
    void render_redirect() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Map<String, String> model = new HashMap<>();

        JspView jspView = new JspView(REDIRECT_PREFIX + "/redirectPath");
        jspView.render(model, request, response);

        assertAll(
                () -> verify(response, times(1)).sendRedirect("/redirectPath"),
                () -> verify(requestDispatcher, never()).forward(request, response)
        );
    }

    @Test
    void render_forward() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Map<String, String> model = new HashMap<>();

        when(request.getRequestDispatcher("/path")).thenReturn(requestDispatcher);

        JspView jspView = new JspView("/path");
        jspView.render(model, request, response);

        assertAll(
                () -> verify(response, never()).sendRedirect("/redirectPath"),
                () -> verify(requestDispatcher, times(1)).forward(request, response)
        );
    }
}
