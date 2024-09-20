package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.View;
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

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("뷰 이름이 redirect:로 시작할 경우 리다이렉트 응답을 전송한다.")
    @Test
    void sendRedirectTest() throws Exception {
        View view = new JspView("redirect:/jazz");

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/jazz");
    }

    @DisplayName("redirect:로 시작하지 않을 경우 Jsp 포워딩을 호출한다.")
    @Test
    void jspForwardTest() throws Exception {
        View view = new JspView("/WEB-INF/views/jazz.jsp");
        Map<String, Object> model = Map.of("name", "jazz", "age", "24");
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/WEB-INF/views/jazz.jsp")).thenReturn(requestDispatcher);

        view.render(model, request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
