package nextstep.mvc.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void redirect가_포함되면_redirect를_실행한다() throws Exception {
        // given
        String viewName = "redirect:index.html";
        JspView jspView = new JspView(viewName);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        doNothing().when(httpServletResponse).sendRedirect("index.html");

        // when
        jspView.render(new HashMap<>(), httpServletRequest, httpServletResponse);

        // then
        verify(httpServletRequest, never()).getRequestDispatcher(anyString());
    }
}
