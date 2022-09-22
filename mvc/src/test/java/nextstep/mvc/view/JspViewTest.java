package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("redirect의 경우 렌더링한다.")
    @Test
    void renderRedirect() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final JspView jspView = new JspView("redirect:/get-test");
        final Map<String, Object> model = new HashMap<>();

        // when
        jspView.render(model, request, response);

        // then
        verify(response, times(1)).sendRedirect("/get-test");
    }

    @DisplayName("forward의 경우 렌더링한다.")
    @Test
    void renderForward() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/post-test")).thenReturn(requestDispatcher);

        final JspView jspView = new JspView("/post-test");
        final Map<String, Object> model = new HashMap<>();

        // when
        jspView.render(model, request, response);

        // then
        verify(request, times(1)).getRequestDispatcher("/post-test");
    }

}
