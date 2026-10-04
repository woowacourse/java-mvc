package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void render_success_modelAttributesAreAddedToRequest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/test.jsp"))
                .thenReturn(requestDispatcher);

        final JspView jspView = new JspView("/test.jsp");

        final Map<String, Object> model = Map.of(
                "id", "gugu",
                "name", "pobi"
        );

        jspView.render(model, request, response);

        verify(request).setAttribute("id", "gugu");
        verify(request).setAttribute("name", "pobi");
    }

    @Test
    void render_success_forwardToJsp() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/test.jsp"))
                .thenReturn(requestDispatcher);

        final JspView jspView = new JspView("/test.jsp");

        jspView.render(Map.of(), request, response);

        verify(request).getRequestDispatcher("/test.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void render_success_redirectWhenViewNameHasRedirectPrefix() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final JspView jspView = new JspView("redirect:/login");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/login");
    }
}