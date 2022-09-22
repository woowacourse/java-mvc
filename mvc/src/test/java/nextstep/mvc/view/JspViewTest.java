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
    void render는_모델의_데이터를_추가하고_입력받은_viewName으로_뷰를_렌더링한다() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        final String viewName = "test";
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        final var test = new JspView(viewName);
        test.render(Map.of("foo", "bar"), request, response);

        // then
        verify(request).setAttribute("foo", "bar");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void render는_받은_viewName이_redirect인_경우_sendRedirect를_수행한다() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final String viewName = "redirect:helloWorld";

        // when
        final var test = new JspView(viewName);
        test.render(Map.of("foo", "bar"), request, response);

        // then
        verify(response).sendRedirect("helloWorld");
    }
}
