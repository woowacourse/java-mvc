package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("JspView render() 메서드 테스트")
    @Test
    void render() throws Exception {
        JspView view = new JspView("/test.jsp");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/test.jsp")).thenReturn(dispatcher);

        Map<String, Object> model = new HashMap<>();
        model.put("key", "value");

        view.render(model, request, response);

        verify(request).setAttribute("key", "value");
        verify(dispatcher).forward(request, response);
    }

    @DisplayName("JspView redirect() 메서드 테스트")
    @Test
    void redirect() throws Exception {
        JspView view = new JspView("redirect:/newUrl");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, Object> model = new HashMap<>();

        view.render(model, request, response);

        verify(response).sendRedirect("/newUrl");
    }
}
