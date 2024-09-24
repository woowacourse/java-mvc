package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
    }

    @Nested
    @DisplayName("render 메소드 테스트")
    class RenderTest {

        @Test
        @DisplayName("일반 JSP 뷰 렌더링")
        void renderNormalJspView() throws Exception {
            // given
            String viewName = "/bbiyong.jsp";
            JspView jspView = new JspView(viewName);
            Map<String, ?> model = Map.of("key", "value");

            when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

            // when
            jspView.render(model, request, response);

            // then
            verify(request).setAttribute("key", "value");
            verify(request, times(1)).getRequestDispatcher(viewName);
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("리다이렉트 뷰 렌더링")
        void renderRedirectView() throws Exception {
            // given
            String viewName = "redirect:/bbiyong.jsp";
            JspView jspView = new JspView(viewName);

            // when
            jspView.render(null, request, response);

            // then
            verify(response, times(1)).sendRedirect("/bbiyong.jsp");
            verifyNoInteractions(requestDispatcher);
        }
    }
}
