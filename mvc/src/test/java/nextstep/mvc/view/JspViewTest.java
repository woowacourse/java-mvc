package nextstep.mvc.view;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("redirect 경로로 요청이 올 경우 response의 redirect를 호출한다.")
    void redirect() throws Exception {
        // given
        final String resourceName = "test.jsp";
        final String viewName = "redirect:" + resourceName;
        View jspView = new JspView(viewName);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        doNothing().when(response).sendRedirect(resourceName);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(response, times(1)).sendRedirect(resourceName);
    }

    @Test
    @DisplayName("redirect 경로가 아닌 요청이 올 경우 RequestDispatcher의 forward를 호출한다.")
    void forward() throws Exception {
        // given
        final String viewName = "";
        final View jspView = new JspView(viewName);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(request, times(1)).getRequestDispatcher("");
        verify(requestDispatcher, times(1)).forward(request, response);
    }

}
