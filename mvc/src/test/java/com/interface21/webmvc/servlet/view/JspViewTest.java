package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void 빈_모델이어도_JSP로_요청을_전달한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(requestDispatcher);

        final var view = new JspView("/user.jsp");
        view.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 모델의_데이터를_요청_속성에_넣는다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(requestDispatcher);

        final var view = new JspView("/user.jsp");
        view.render(Map.of("name", "봉구스", "age", 25), request, response);

        verify(request).setAttribute("name", "봉구스");
        verify(request).setAttribute("age", 25);
    }

    @Test
    void 리다이렉트하면_JSP로_요청을_전달하지_않는다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var view = new JspView("redirect:/login");
        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
