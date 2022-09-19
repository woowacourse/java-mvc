package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void 뷰_이름이_redirect로_시작하면_redirect한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String viewName = "redirect:index.jsp";
        JspView jspView = new JspView(viewName);

        jspView.render(new HashMap<>(), request, response);

        verify(response).sendRedirect("index.jsp");
    }

    @Test
    void 뷰_이름이_redirect로_시작하지_않으면_forward한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        String viewName = "index.jsp";
        JspView jspView = new JspView(viewName);

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        jspView.render(new HashMap<>(), request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
