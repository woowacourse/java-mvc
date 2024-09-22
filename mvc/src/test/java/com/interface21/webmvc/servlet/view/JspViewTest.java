package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("viewName이 `redirect:`로 시작하면 리다이렉트한다.")
    @Test
    void renderWithRedirect() throws Exception {
        JspView jspView = new JspView("redirect:/test.jsp");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        jspView.render(Map.of(), request, response);

        String viewName = "redirect:/test.jsp".substring(JspView.REDIRECT_PREFIX.length());

        verify(response).sendRedirect(viewName);
    }

    @DisplayName("request에 model을 바인딩한다.")
    @Test
    void bindModelToRequest() throws Exception {
        JspView jspView = new JspView("/get-test");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/get-test")).thenReturn(requestDispatcher);
        jspView.render(Map.of("id", "id"), request, response);

        verify(request).setAttribute("id", "id");
    }

    @DisplayName("viewName의 jsp 파일 경로로 포워딩한다.")
    @Test
    void renderWithForward() throws Exception {
        JspView jspView = new JspView("/get-test");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/get-test")).thenReturn(requestDispatcher);
        jspView.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
