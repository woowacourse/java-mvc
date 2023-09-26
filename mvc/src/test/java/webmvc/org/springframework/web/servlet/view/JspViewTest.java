package webmvc.org.springframework.web.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void Jsp_리소스로_응답한다() throws Exception {
        // given
        String viewName = "index.jsp";
        JspView view = new JspView(viewName);
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        view.render(new HashMap<>(), request, response);

        // then
        verify(request).getRequestDispatcher(viewName);
        verify(requestDispatcher).forward(request, response);
    }
}
