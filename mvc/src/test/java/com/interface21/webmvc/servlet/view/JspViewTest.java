package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void 모델을_request에_담은_뒤_JSP로_forward한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(dispatcher);

        new JspView("/user.jsp").render(Map.of("id", "gugu"), request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(request).getRequestDispatcher("/user.jsp");
        order.verify(dispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void 모델이_비어_있어도_JSP로_forward한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        new JspView("/index.jsp").render(Map.of(), request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect("/index.jsp");
    }

    @Test
    void redirect_접두사가_있으면_대상_URL로_redirect한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/login?next=/users").render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/login?next=/users");
        verifyNoInteractions(request);
    }
}
