package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void 리다이렉트_처리_테스트() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String viewName = "redirect:/test";
        JspView jspView = new JspView(viewName);

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/test");
    }

    @Test
    void 모델_데이터_설정_테스트() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        String viewName = "/test.jsp";
        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

        Map<String, Object> model = new HashMap<>();
        model.put("id", 1);
        model.put("name", "ted");

        JspView jspView = new JspView(viewName);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("id", 1);
        verify(request).setAttribute("name", "ted");
    }

    @Test
    void 리다이렉트_아닌_경우_포워딩_테스트() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        String viewName = "/test.jsp";
        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

        JspView jspView = new JspView(viewName);

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(dispatcher).forward(request, response);
    }
}
