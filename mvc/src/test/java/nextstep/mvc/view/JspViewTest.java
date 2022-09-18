package nextstep.mvc.view;

import static nextstep.mvc.view.JspView.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("REDIRECT_PREFIX 값이 앞에 붙어있다면 redirect한다.")
    void redirect() throws Exception {
        String viewName = "test.jsp";
        JspView jspView = new JspView(REDIRECT_PREFIX + viewName);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        jspView.render(new HashMap<>(), request, response);

        verify(response).sendRedirect(viewName);
    }

    @Test
    @DisplayName("REDIRECT_PREFIX 값이 앞에 붙어있지않다면 forward 한다.")
    void forward() throws Exception {
        // given
        String viewName = "test.jsp";
        JspView jspView = new JspView(viewName);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        given(request.getRequestDispatcher(viewName)).willReturn(dispatcher);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(dispatcher).forward(request, response);
    }
}
