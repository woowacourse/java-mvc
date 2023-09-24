package webmvc.org.springframework.web.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.View;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewTest {

    @Test
    void redirect_설정이_되어있는_경우_response의_sendRedirect_메서드를_호출한다() throws Exception {
        // given
        final View view = JspView.redirectTo("hello");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        view.render(Map.of(), request, response);

        // then
        verify(response, only()).sendRedirect("hello");
    }

    @Test
    void redirect가_설정되어있지_않는_경우_forward_메서드를_호출한다() throws Exception {
        // given
        final View view = new JspView("hello");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher(anyString())).willReturn(requestDispatcher);

        // when
        view.render(Map.of(), request, response);

        // then
        verify(requestDispatcher, only()).forward(request, response);
    }
}
