package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("모델을 request 속성에 저장한 뒤 JSP로 포워드한다")
    void forward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        new JspView("/index.jsp").render(Map.of("name", "홍길동"), request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("name", "홍길동");
        order.verify(request).getRequestDispatcher("/index.jsp");
        order.verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("redirect 접두사를 제거한 경로로 리다이렉트한다")
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/login").render(Map.of("name", "홍길동"), request, response);

        verify(response).sendRedirect("/login");
        verifyNoInteractions(request);
    }
}
