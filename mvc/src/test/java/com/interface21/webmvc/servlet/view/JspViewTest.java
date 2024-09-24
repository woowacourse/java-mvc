package com.interface21.webmvc.servlet.view;

import static com.interface21.webmvc.servlet.view.JspView.REDIRECT_PREFIX;
import static org.mockito.ArgumentMatchers.any;
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

    @DisplayName("viewName에 리다이렉트 접두사가 붙은 경우, 리다이렉트한다.")
    @Test
    void render_redirect() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        String url = "/nyangin";
        String viewName = REDIRECT_PREFIX + url;
        JspView jspView = new JspView(viewName);

        jspView.render(null, request, response);

        verify(response).sendRedirect(url);
        verify(request, never()).setAttribute(anyString(), any());
        verify(requestDispatcher, never()).forward(any(), any());
    }

    @DisplayName("모델에 속성을 설정하고, 뷰를 포워딩한다.")
    @Test
    void render_setAttributeAndForward() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        String viewName = "/index.jsp";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = Map.of(
                "id", "nyangin"
        );

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        verify(response, never()).sendRedirect(anyString());
        verify(request).setAttribute("id", "nyangin");
        verify(requestDispatcher).forward(request, response);
    }
}
