package nextstep.mvc.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {


    @DisplayName("특정 파일 이름이 주어질 시 해당 파일을 보여준다.")
    @Test
    void render() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));

        //when
        JspView jspView = new JspView("/index.jsp");
        jspView.render(Map.of("key", "value"), request, response);

        //then
        verify(request).getRequestDispatcher("/index.jsp");
    }

    @DisplayName("특정 주소로 리다이렉트 할 수 있다.")
    @Test
    void render_redirect() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        //when
        JspView jspView = new JspView("redirect:/index.jsp");
        jspView.render(Map.of("key", "value"), request, response);

        //then
        verify(response).sendRedirect("/index.jsp");
    }
}
