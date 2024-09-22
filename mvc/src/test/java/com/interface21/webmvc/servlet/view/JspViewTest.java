package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
    }

    @DisplayName("뷰 이름이 redirect:로 시작하면 리다이렉트한다.")
    @Test
    void redirect() throws Exception {
        JspView jspView = new JspView("redirect:/home");

        jspView.render(new HashMap<>(), request, response);

        assertAll(
                () -> verify(response).sendRedirect("/home"),
                () -> verify(request, never()).getRequestDispatcher(anyString())
        );
    }

    @DisplayName("뷰 이름이 redirect:로 시작하지 않으면 포워딩한다.")
    @Test
    void forward() throws Exception {
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        JspView jspView = new JspView("/WEB-INF/views/home.jsp");
        when(request.getRequestDispatcher("/WEB-INF/views/home.jsp")).thenReturn(requestDispatcher);

        jspView.render(Map.of("name", "jerry"), request, response);

        assertAll(
                () -> verify(request).setAttribute("name", "jerry"),
                () -> verify(requestDispatcher).forward(request, response)
        );
    }
}
