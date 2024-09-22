package com.interface21.webmvc.servlet.mvc.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class JspViewTest {

    @Test
    @DisplayName("리다이렉트 처리할 수 있다.")
    void renderRedirectTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        JspView view = new JspView("redirect:/login.jsp");

        // when
        view.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/login.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("포워딩을 처리할 수 있다.")
    void renderForwardTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        Map<String, Object> model = Map.of("id", "groom");
        JspView view = new JspView("/login.jsp");

        // when
        view.render(model, request, response);

        // then
        assertAll(
                () -> verify(request).setAttribute("id", "groom"),
                () -> verify(dispatcher).forward(request, response)
        );
    }
}
