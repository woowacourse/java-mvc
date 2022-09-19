package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("리다이렉트를 할 수 있다.")
    void redirect() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/zero");
        when(request.getMethod()).thenReturn("GET");

        final JspView jspView = new JspView("redirect:/");

        assertDoesNotThrow(() -> jspView.render(Map.of(), request, response));
    }

    @DisplayName("forward 할 수 있다.")
    @Test
    void forward() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/login"))
                .thenReturn(requestDispatcher);

        final JspView jspView = new JspView("/login");

        assertDoesNotThrow(() -> jspView.render(Map.of(), request, response));
        verify(requestDispatcher).forward(request, response);
    }
}
