package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("Model을 Request에 세팅하고 포워딩한다.")
    @Test
    void renderTest() throws Exception {
        // given
        HttpServletRequest request = spy(HttpServletRequest.class);
        HttpServletResponse response = spy(HttpServletResponse.class);

        Map<String, ?> model = Map.of("id", "groom");
        JspView view = new JspView("/login.jsp");

        // when
        view.render(model, request, response);

        // then
        assertAll(
            () -> verify(request).setAttribute("id", "groom"),
            () -> verify(request).getRequestDispatcher("/login.jsp")
        );
    }

    @DisplayName("uri가 redirect:로 시작하면 바로 리다이렉션한다.")
    @Test
    void renderTest1() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, ?> model = Map.of();
        JspView view = new JspView("redirect:/404.jsp");

        // when
        view.render(model, request, response);

        // then
        assertAll(
            () -> verify(response).sendRedirect("/404.jsp"),
            () -> verify(request, never()).getRequestDispatcher(anyString())
        );
    }

    @DisplayName("조회한 페이지가 없으면 404 페이지를 반환한다.")
    @Test
    void renderTest2() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, ?> model = Map.of();
        String viewName = "/notfound.jsp";
        JspView jspView = new JspView(viewName);
        when(request.getRequestDispatcher(viewName)).thenReturn(null);

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/404.jsp");
    }
}
