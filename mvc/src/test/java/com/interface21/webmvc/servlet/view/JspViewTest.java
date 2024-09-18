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
    void 모델_테스트() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        String viewName = "/test";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();
        model.put("model", model);
        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("model", model);
    }

    @Test
    void 리다이렉트_테스트() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JspView jspView = new JspView("redirect:/");

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/");
    }
}
