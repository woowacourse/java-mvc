package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void viewName이_redirect로_시작하면_응답을_리다이렉트한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String viewName = "redirect:/redirect-url";
        View jspView = new JspView(viewName);

        // when
        jspView.render(Collections.emptyMap(), request, response);

        // then
        verify(response).sendRedirect("/redirect-url");
    }

    @Test
    void 모델의_key_value를_request의_attribute에_담는다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Map<String, Object> model = Map.of("id", 1L, "name", "prin");
        View jspView = new JspView("viewName");

        // when
        jspView.render(model, request, response);

        // then
        assertSoftly(softly -> {
            verify(request).setAttribute("id", 1L);
            verify(request).setAttribute("name", "prin");
        });
    }
}
