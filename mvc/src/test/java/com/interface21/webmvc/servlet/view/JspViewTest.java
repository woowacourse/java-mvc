package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("response를 viewName으로 forward한다.")
    @Test
    void render() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        String expectedUrl = "index.jsp";
        JspView view = new JspView(expectedUrl);

        when(request.getRequestDispatcher(expectedUrl)).thenReturn(requestDispatcher);
        // when
        view.render(new HashMap<>(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("viewName에 redirect:가 포함되면, 헤더에 location을 추가한다.")
    @Test
    void render_withRedirect() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String expectedUrl = "index.jsp";
        JspView view = new JspView("redirect:" + expectedUrl);

        // when
        view.render(new HashMap<>(), request, response);

        // then
        verify(response).sendRedirect(expectedUrl);
    }

    @DisplayName("model을 request attribute로 추가한다.")
    @Test
    void render_setAttributeByModel() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        JspView view = new JspView("index.jsp");
        String name = "atom";
        int age = 25;

        when(request.getRequestDispatcher("index.jsp")).thenReturn(requestDispatcher);
        // when
        view.render(Map.of("name", name, "age", age), request, response);

        // then
        verify(request).setAttribute("name", name);
        verify(request).setAttribute("age", age);
        verify(requestDispatcher).forward(request, response);
    }
}
