package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JspViewTest {

    private JspView jspView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void render_일반JSP뷰_정상동작() throws Exception {
        // given
        jspView = new JspView("login");
        HashMap<String, Object> model = new HashMap<>();

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(eq("/webapp/login.jsp"))).thenReturn(dispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).getRequestDispatcher("/webapp/login.jsp");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void render_리다이렉트뷰_정상동작() throws Exception {
        // given
        jspView = new JspView("redirect:/404");
        Map<String, Object> model = new HashMap<>();

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/404");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
