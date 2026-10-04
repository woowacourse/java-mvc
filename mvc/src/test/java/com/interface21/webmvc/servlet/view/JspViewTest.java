package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JspViewTest {

    @Test
    void 모델을_request_attribute로_전달한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final JspView jspView = new JspView("/index.jsp");

        jspView.render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
    }

    @Test
    void redirect_뷰는_지정한_경로로_리다이렉트한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final JspView jspView = new JspView("redirect:/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
