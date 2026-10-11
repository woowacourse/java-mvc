package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("모델을 요청 속성에 저장하고 JSP로 포워드한다")
    void forwardsToJspWithModelAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(requestDispatcher);
        final var view = new JspView("/users.jsp");

        view.render(Map.of("id", 1L, "name", "gugu"), request, response);

        verify(request).setAttribute("id", 1L);
        verify(request).setAttribute("name", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("redirect 접두사가 있으면 포워드하지 않고 리다이렉트한다")
    void redirectsWithoutForwarding() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/users");

        view.render(Map.of("id", 1L), request, response);

        verify(response).sendRedirect("/users");
        verify(request, never()).setAttribute("id", 1L);
        verify(request, never()).getRequestDispatcher("redirect:/users");
    }
}
