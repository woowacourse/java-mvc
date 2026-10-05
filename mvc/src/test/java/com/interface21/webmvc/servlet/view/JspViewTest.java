package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void redirect_접두사가_있으면_접두사를_제외한_경로로_리다이렉트한다() throws Exception {
        final var view = new JspView("redirect:/index.jsp");

        view.render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
        verify(request, never()).setAttribute(anyString(), any());
    }

    @Test
    void redirect_접두사가_없으면_뷰_이름으로_포워드한다() throws Exception {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        final var view = new JspView("/login.jsp");

        view.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 포워드할_때_모델을_request_attribute로_전달한다() throws Exception {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        final var view = new JspView("/login.jsp");

        view.render(Map.of("id", "gugu", "age", 20), request, response);

        verify(request).setAttribute("id", "gugu");
        verify(request).setAttribute("age", 20);
        verify(requestDispatcher).forward(request, response);
    }
}
