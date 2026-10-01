package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("모델을 요청 속성에 담고 JSP로 포워드한다")
    void forwardsModelToJsp() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(dispatcher);

        // when
        new JspView("/user.jsp").render(Map.of("id", "gugu"), request, response);

        // then
        verify(request).setAttribute("id", "gugu");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("redirect: 접두어가 있으면 지정한 경로로 리다이렉트한다")
    void redirectsWhenViewNameHasRedirectPrefix() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
