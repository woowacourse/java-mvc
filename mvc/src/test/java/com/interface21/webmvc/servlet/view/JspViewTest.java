package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.*;
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

@DisplayName("Jsp 뷰")
class JspViewTest {

    @DisplayName("Jsp 뷰는 리다이렉트 요청을 렌더링한다.")
    @Test
    void renderRedirect() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JspView jspView = new JspView("redirect:/index.jsp");

        // when
        jspView.render(null, request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
    }

    @DisplayName("Jsp 뷰는 포워드 요청을 렌더링한다.")
    @Test
    void renderForward() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        JspView jspView = new JspView("/index.jsp");

        Map<String, String> model = new HashMap<>();
        model.put("name1", "value1");
        model.put("name2", "value2");

        // when
        jspView.render(model, request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}
