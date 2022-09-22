package nextstep.mvc.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JspViewTest {

    @DisplayName("JspView의 viewName이 redirect가 아닌 일반 jsp경로이면 forward만 실행되고 sendRedirect는 실행되지 않는다.")
    @Test
    void render_notRedirect() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final String viewName = "login.jsp";
        final JspView jspView = new JspView(viewName);

        given(request.getRequestDispatcher(viewName))
                .willReturn(requestDispatcher);

        // when
        jspView.render(Collections.emptyMap(), request, response);

        // then
        verify(requestDispatcher, times(1)).forward(request, response);
        verify(response, never()).sendRedirect(viewName);
    }

    @DisplayName("JspView의 viewName이 redirect가 포함되어있으면 sendRedirect만 실행되고 forward는 실행되지 않는다.")
    @Test
    void render_redirect() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final String viewName = "redirect:login.jsp";
        final JspView jspView = new JspView(viewName);

        given(request.getRequestDispatcher(viewName))
                .willReturn(requestDispatcher);

        // when
        jspView.render(Collections.emptyMap(), request, response);

        // then
        verify(response, times(1)).sendRedirect("login.jsp");
        verify(requestDispatcher, never()).forward(request, response);
    }
}
