package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("요청 URI 가 redirect: 로 시작할 경우 응답을 Redirect 한다.")
    void redirect() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String endpoint = "/test.jsp";
        JspView jspView = new JspView(JspView.REDIRECT_PREFIX + endpoint);

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect(endpoint);
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("일반 요청에 대해 JSP 로 요청을 전달한다.")
    void forward() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String viewName = "/test.jsp";
        JspView jspView = new JspView(viewName);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Map<String, String> model = Map.of("k1", "v1", "k2", "v2");

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(response, never()).sendRedirect(anyString());
        model.forEach((k, v) -> verify(request).setAttribute(k, v));
        verify(requestDispatcher).forward(request, response);
    }
}
