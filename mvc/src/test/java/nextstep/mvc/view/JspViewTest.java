package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Nested
    @DisplayName("render 메소드는")
    class Render {

        @Test
        @DisplayName("Redirect 응답의 경우 Response의 Header에 Location 필드를 추가한다.")
        void success_redirect() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            final JspView jspView = new JspView("redirect:/index.html");

            // when
            jspView.render(new HashMap<>(), request, response);

            // then
            verify(response, times(1)).sendRedirect("/index.html");
        }

        @Test
        @DisplayName("Forward 요청의 경우 RequestDispatcher의 forward를 실행한다.")
        void success_forward() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/index.html")).thenReturn(requestDispatcher);

            final JspView jspView = new JspView("/index.html");

            // when
            jspView.render(new HashMap<>(), request, response);

            // then
            verify(requestDispatcher, times(1)).forward(request, response);
        }
    }
}