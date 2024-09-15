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

@DisplayName("JspView 테스트")
class JspViewTest {

    @DisplayName("Redirect 처리 테스트")
    @Test
    void renderRedirectTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String viewName = "redirect:/test-url";
        JspView jspView = new JspView(viewName);

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/test-url");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @DisplayName("JSP Forward 처리 테스트")
    @Test
    void renderForwardTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        String viewName = "/WEB-INF/views/test.jsp";
        JspView jspView = new JspView(viewName);

        // 모델 데이터를 생성
        Map<String, Object> model = new HashMap<>();
        model.put("key1", "value1");
        model.put("key2", "value2");

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("key1", "value1");
        verify(request).setAttribute("key2", "value2");
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
