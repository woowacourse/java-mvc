package com.interface21.webmvc.servlet.view;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("redirect 접두사가 존재하는 경우 Redirect 처리한다.")
    @Test
    void redirect() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String viewName = "redirect:/index.jsp";
        JspView jspView = new JspView(viewName);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
    }

    @DisplayName("redirect 접두사가 존재하지 않는 경우 모델 데이터를 request attribute로 설정하고 forward 처리한다.")
    @Test
    void forward() throws Exception {
        // given
        String viewName = "/index.jsp";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        JspView jspView = new JspView(viewName);

        // when
        jspView.render(Map.of("id", "pedro"), request, response);

        // then
        verify(request).setAttribute("id", "pedro");
        verify(requestDispatcher).forward(request, response);
    }
}
