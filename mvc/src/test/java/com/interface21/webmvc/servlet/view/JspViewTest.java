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
    void redirectPrefix가_존재하면_리다이렉트를_수행한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Map<String, ?> model = new HashMap<>();

        String viewName = "redirect:/index.jsp";

        JspView jspView = new JspView(viewName);
        jspView.render(model, request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void model값이_존재하면_request에_추가한다() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        Map<String, ?> model = Map.of(
                "name", "eden",
                "age", 11
        );
        String viewName = "/index.jsp";
        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

        JspView jspView = new JspView(viewName);
        jspView.render(model, request, response);

        verify(request).setAttribute("name", model.get("name"));
        verify(request).setAttribute("age", model.get("age"));
    }

    @Test
    void redirect가_아닌_경우_forward한다() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        Map<String, ?> model = Map.of(
                "name", "eden",
                "age", 11
        );
        String viewName = "/index.jsp";
        when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

        JspView jspView = new JspView(viewName);
        jspView.render(model, request, response);

        verify(dispatcher).forward(request, response);
    }
}
