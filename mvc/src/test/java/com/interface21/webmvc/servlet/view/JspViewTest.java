package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("모델을 요청에 담아 JSP로 전달한다")
    void forward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var view = new JspView("/WEB-INF/views/user.jsp");
        when(request.getRequestDispatcher("/WEB-INF/views/user.jsp")).thenReturn(requestDispatcher);

        view.render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("redirect 경로로 새로운 요청을 보낸다")
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/users");

        view.render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/users");
        verify(request, never()).setAttribute(anyString(), any());
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
