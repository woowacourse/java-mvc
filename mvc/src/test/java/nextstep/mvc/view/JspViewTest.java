package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("view의 렌더링을 수행한다")
    void render() {
        // given
        View view = new JspView("hello");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));

        // when & then
        assertThatNoException().isThrownBy(
                () -> view.render(Map.of("id", "gugu"), request, response)
        );
    }

    @Test
    @DisplayName("view의 렌더링 결과로 redirect를 수행한다.")
    void redirect() throws IOException {
        // given
        View view = new JspView("redirect:hello");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));

        // when
        view.render(Map.of("id", "gugu"), request, response);

        // then
        verify(response, times(1)).sendRedirect(any());
    }
}
