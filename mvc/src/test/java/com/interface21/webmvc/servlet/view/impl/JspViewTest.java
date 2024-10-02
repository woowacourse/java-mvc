package com.interface21.webmvc.servlet.view.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("리다이렉트 접두사가 붙어있으면, 라디이렉트 한다.")
    void redirect() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        JspView jspView = new JspView("redirect:/index.jsp");
        jspView.render(null, request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("리다이렉트 접두사가 붙어있지 않으면, 포워드 한다.")
    void forward() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher("/register.jsp"))
                .willReturn(requestDispatcher);

        JspView jspView = new JspView("/register.jsp");
        Map<String, String> model = Map.of("name", "sancho");
        jspView.render(model, request, response);

        verify(request).getRequestDispatcher("/register.jsp");
        verify(request).setAttribute("name", "sancho");
    }
}
