package nextstep.mvc.view;

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

    @DisplayName("redirect로 시작할 경우 sendRedirect 메서드를 호출")
    @Test
    void redirect() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final View view = new JspView("redirect:/");

        // when
        view.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/");
    }

    @DisplayName("redirect로 시작하지 않는 경우 dispatcher.forward를 호출")
    @Test
    void forward() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        final View view = new JspView("/");

        // when
        when(request.getRequestDispatcher("/"))
                .thenReturn(dispatcher);

        view.render(Map.of(), request, response);

        // then
        verify(dispatcher).forward(request, response);
    }
}
