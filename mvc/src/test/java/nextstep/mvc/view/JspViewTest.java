package nextstep.mvc.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName(value = "view 이름이 redirect:로 시작하지 않으면 forward() 실행")
    @Test
    void dontStartWithRedirect() throws Exception {
        // given
        final String jspName = "login.jsp";
        final JspView jspView = new JspView(jspName);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestDispatcher(jspName)).willReturn(requestDispatcher);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName(value = "view 이름이 redirect:로 시작하면 sendRedirect() 실행")
    @Test
    void startWithRedirect() throws Exception {
        // given
        final String jspName = "test.jsp";
        final JspView jspView = new JspView("redirect:" + jspName);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(response).sendRedirect(jspName);
    }
}
