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

class JspViewTest {
    @DisplayName("리다이렉트 뷰를 렌더링한다.")
    @Test
    void redirectView() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JspView jspView = new JspView("redirect:/newPage");

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/newPage");
    }

    @DisplayName("정상 JSP 뷰를 렌더링한다.")
    @Test
    void renderView() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        String viewName = "test.jsp";
        JspView jspView = new JspView(viewName);

        Map<String, Object> model = new HashMap<>();
        model.put("key", "value");
        model.put("key2", "value2");

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("key", "value");
        verify(request).setAttribute("key2", "value2");
        verify(requestDispatcher).forward(request, response);
    }
}
