package nextstep.mvc.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("render 메서드는")
    @Nested
    class Render {

        @DisplayName("viewName이 redirect: 로 시작하면 redirect 응답을 내보낸다")
        @Test
        void jspView_sends_redirect_when_viewName_starts_with_redirect() throws Exception {
            // given
            final JspView jspView = new JspView("redirect:/index.jsp");
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            jspView.render(new HashMap<>(), request, response);

            // then
            assertAll(
                    () -> verify(response, times(1)).sendRedirect("/index.jsp"),
                    () -> verify(request, never()).getRequestDispatcher(anyString())
            );
        }

        @DisplayName("viewName이 redirect: 로 시작하지 않으면, Model 값 전부를 request에 추가한 뒤 forward한다")
        @Test
        void jspView_sends_redirect_when_viewName_starts_with_redirect2() throws Exception {
            // given
            final JspView jspView = new JspView("/index.jsp");
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
            given(request.getRequestDispatcher("/index.jsp")).willReturn(requestDispatcher);

            // when
            jspView.render(Map.of("key1", "value2", "key2", "value2"), request, response);

            // then
            assertAll(
                    () -> verify(response, never()).sendRedirect(anyString()),
                    () -> verify(request, times(2)).setAttribute(anyString(), anyString()),
                    () -> verify(request, times(1)).getRequestDispatcher(anyString()),
                    () -> verify(requestDispatcher, times(1)).forward(request, response)
            );
        }
    }
}
