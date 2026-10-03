package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    @DisplayName("모델을 request 속성에 담고 뷰로 forward 한다")
    void render_forward() throws Exception {
        // given
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        JspView jspView = new JspView("/index.jsp");
        Map<String, Object> model = Map.of("id", "gugu");

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("redirect: 접두사가 있으면 해당 경로로 redirect 한다")
    void render_redirect() throws Exception {
        // given
        JspView jspView = new JspView("redirect:/index.jsp");

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher("/index.jsp");
    }
}
