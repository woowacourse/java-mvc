package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    @DisplayName("JspView의 작동을 검증한다")
    @Test
    void verify_jsp_view_render() throws Exception {
        // given
        String viewName = "view";
        JspView jspView = new JspView(viewName);

        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(dispatcher).forward(request, response);
    }
}
