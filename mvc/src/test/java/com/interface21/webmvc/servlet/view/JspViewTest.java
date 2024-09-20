package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JspViewTest {
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String REQUEST_URI_REDIRECT_TEST = "redirect:/test";
    private static final String KEY = "id";
    private static final String VALUE = "id";

    @DisplayName("request에 속성을 저장한다.")
    @Test
    void renderTestWithSetAttribute() throws Exception {
        // given
        final JspView jspView = new JspView(REQUEST_URI_GET_TEST);
        final Map<String, String> model = new HashMap<>();
        final var request = mock(HttpServletRequest.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var response = mock(HttpServletResponse.class);
        model.put(KEY, VALUE);
        when(request.getRequestDispatcher(REQUEST_URI_GET_TEST)).thenReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute(KEY, VALUE);
    }

    @DisplayName("view name의 jsp 파일로 포워딩한다.")
    @Test
    void renderTestWithForward() throws Exception {
        // given
        final JspView jspView = new JspView(REQUEST_URI_GET_TEST);
        final var request = mock(HttpServletRequest.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher(REQUEST_URI_GET_TEST)).thenReturn(requestDispatcher);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("view name이 \"redirect:\"로 시작하면 리다이렉트한다.")
    @Test
    void renderTestWithRedirect() throws Exception {
        // given
        final JspView jspView = new JspView(REQUEST_URI_REDIRECT_TEST);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        final String viewName = REQUEST_URI_REDIRECT_TEST.substring(JspView.REDIRECT_PREFIX.length());

        verify(response).sendRedirect(viewName);
    }
}
