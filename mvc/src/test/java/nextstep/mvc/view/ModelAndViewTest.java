package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ModelAndViewTest {

    @Test
    void view를_render_할_수_있다() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final JspView view = mock(JspView.class);
        final ModelAndView modelAndView = new ModelAndView(view);

        // when
        modelAndView.render(request, response);

        // then
        verify(view).render(Map.of(), request, response);
    }
}
