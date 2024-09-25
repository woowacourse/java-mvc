package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

class JspViewTest {

    private JspView jspView;
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
    @DisplayName("JSP View를 정상적으로 렌더링한다.")
    void renderJspView() throws Exception {
        // given
        String viewName = "/WEB-INF/views/test.jsp";
        jspView = new JspView(viewName);

        Map<String, Object> model = new HashMap<>();
        model.put("name", "Test");
        model.put("age", 25);

        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        jspView.render(model, request, response);

        // then
        verify(request).setAttribute("name", "Test");
        verify(request).setAttribute("age", 25);
        verify(request).getRequestDispatcher(viewName);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("리디렉션 동작을 처리한다.")
    void renderRedirectView() throws Exception {
        // given
        String viewName = "redirect:/home";
        jspView = new JspView(viewName);

        Map<String, Object> model = new HashMap<>();

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/home");
        verify(request, never()).setAttribute(anyString(), any());
    }
}
