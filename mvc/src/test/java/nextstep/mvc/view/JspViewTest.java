package nextstep.mvc.view;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("redirect prefix가 붙어있는 경우 redirect를 진행한다.")
    void redirect() throws Exception {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Map<String, Object> model = new HashMap<>();

        JspView jspView = new JspView("redirect:test");

        jspView.render(model, httpServletRequest, httpServletResponse);

        verify(httpServletResponse, times(1)).sendRedirect("test");
    }

}