package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {
    @DisplayName("forward 전에 model 값을 모두 request attribute로 옮긴다")
    @Test
    void setsModelBeforeForward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(dispatcher);

        new JspView("/users.jsp").render(Map.of("id", "gugu", "count", 2), request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(dispatcher).forward(request, response);
        final var countOrder = inOrder(request, dispatcher);
        countOrder.verify(request).setAttribute("count", 2);
        countOrder.verify(dispatcher).forward(request, response);
    }

    @DisplayName("redirect일 때는 접두사를 떼고 리다이렉트하며, model을 request에 담지 않는다")
    @Test
    void redirectsWithoutSettingModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).setAttribute(anyString(), any());
        verify(request, never()).getRequestDispatcher(anyString());
    }
}