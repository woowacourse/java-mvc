package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void forwardsToJspWithModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(dispatcher);

        final var view = new JspView("/users.jsp");

        view.render(Map.of("name", "맥스"), request, response);

        // JSP가 모델을 사용할 수 있도록 attribute 설정이 forward보다 먼저여야 한다.
        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("name", "맥스");
        order.verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirectsWithoutForwarding() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/users");

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/users");
        // 리다이렉트 후에는 JSP forward 처리를 진행하지 않아야 한다.
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
