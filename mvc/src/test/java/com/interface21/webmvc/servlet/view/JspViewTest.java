package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private static final String REDIRECT_URL = "redirect:/users";
    private static final String FORWARD_URL = "/users";
    private static final HashMap<String, Object> EMPTY_MAP = new HashMap<>();

    @Test
    @DisplayName("Redirect 요청인 경우, sendRedirect가 수행되어야 한다")
    void redirect() throws Exception {
        // given
        final JspView jspView = new JspView(REDIRECT_URL);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        jspView.render(EMPTY_MAP, request, response);

        // then
        verify(response).sendRedirect("/users");
    }

    @Test
    @DisplayName("Redirect 요청이 아닌 경우, sendRedirect가 수행되면 안 된다.")
    void shouldNotRedirect() throws Exception {
        // given
        final JspView jspView = new JspView(FORWARD_URL);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(FORWARD_URL))
                .thenReturn(dispatcher);

        // when
        jspView.render(EMPTY_MAP, request, response);

        // then
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("Map의 모든 속성을 HttpServletRequest로 옮겨야 한다.")
    void addAttributes() throws Exception {
        // given
        final Map<String, ?> model = Map.of("attribute1", "value1", "attribute2", "value2");
        final JspView jspView = new JspView(FORWARD_URL);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(FORWARD_URL)).thenReturn(dispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("attribute1", "value1");
        verify(request).setAttribute("attribute2", "value2");
    }

    @Test
    @DisplayName("지정한 뷰 경로로 forward해야 한다.")
    void forward() throws Exception {
        // given
        final JspView jspView = new JspView(FORWARD_URL);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(FORWARD_URL)).thenReturn(dispatcher);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(request).getRequestDispatcher(FORWARD_URL);
        verify(dispatcher).forward(request, response);
    }
}
