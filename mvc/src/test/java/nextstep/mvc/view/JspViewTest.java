package nextstep.mvc.view;

import static nextstep.mvc.view.JspView.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import nextstep.mvc.view.exception.NotFoundViewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("redirect prefix가 viewName에 들어가면 HttpServletResponse의 sendRedirect 로 url을 넘겨준다.")
    @Test
    void render_Redirect() throws Exception {
        final String viewName = "/east";
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final View view = new JspView(REDIRECT_PREFIX + viewName);
        view.render(Collections.emptyMap(), request, response);

        verify(response).sendRedirect(viewName);
    }

    @DisplayName("redirect prefix가 viewName에 없으면 requestDispatcher가 forward 실행")
    @Test
    void render_Forward() throws Exception {
        final String viewName = "/east";
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final View view = new JspView(viewName);

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        view.render(Collections.emptyMap(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("forward 하는데 해당하는 viewName이 없는 경우 NotFoundViewException 발생")
    @Test
    void render_NotFoundViewException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final View view = new JspView("/");

        assertThatThrownBy(() -> view.render(Collections.emptyMap(), request, response))
                .isInstanceOf(NotFoundViewException.class);
    }
}
