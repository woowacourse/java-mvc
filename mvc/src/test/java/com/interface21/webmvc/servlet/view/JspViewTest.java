package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("viewName에 redirect:가 포함되어 있으면, 그 뒤에 붙은 url로 redirect 요청한다.")
    @Test
    void redirect() throws Exception {
        // given
        JspView jspView = new JspView("redirect:/target");

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/target");
    }

    @DisplayName("viewName에 redirect:가 포함되어 있지 않으면, requestDispatcher.forward()를 호출한다.")
    @Test
    void forward() throws Exception {
        // given
        JspView jspView = new JspView("/target");
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/target")).thenReturn(requestDispatcher);

        // when
        jspView.render(Map.of(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}
