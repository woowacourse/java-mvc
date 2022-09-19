package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void render_redirect() throws Exception {
        final JspView view = new JspView("redirect:yaho");
        final Map<String, Object> model = Map.of("id", "gugu");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        view.render(model, request, response);

        verify(response, times(1)).sendRedirect("yaho");
    }

    @Test
    void render_nonRedirect() {
        final JspView view = new JspView("yaho");
        final Map<String, Object> model = Map.of("id", "gugu");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        assertThatThrownBy(() -> view.render(model, request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
