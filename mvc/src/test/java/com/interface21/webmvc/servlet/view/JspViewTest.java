package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JspViewTest {

    @DisplayName("viewName으로 리다이렉트할 수 있다")
    @Test
    void canRedirectWhenStartsWithRedirectPrefix() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String redirectViewName = "index.jsp";
        JspView redirectJspView = new JspView("redirect:" + redirectViewName);

        redirectJspView.render(Map.of(), request, response);

        Mockito.verify(response, times(1))
                .sendRedirect(eq(redirectViewName));
    }

    @DisplayName("일치하는 RequestDispatcher가 없을 시 404.jsp로 리다이렉트한다")
    @Test
    void redirectTo404_When_NotMatchedDispatcher() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String notMatchedViewName = "noneViewName";
        JspView notFoundJspView = new JspView(notMatchedViewName);

        notFoundJspView.render(Map.of(), request, response);

        Mockito.verify(response, times(1))
                .sendRedirect(eq("404.jsp"));
    }

    @DisplayName("일치하는 RequestDispatcher가 있다면 forward 한다")
    @Test
    void forward_When_MatchedDispatcher() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dummyDispatcher = mock(RequestDispatcher.class);

        String viewName = "index.html";
        Mockito.when(request.getRequestDispatcher(eq(viewName)))
                .thenReturn(dummyDispatcher);

        JspView foundjspview = new JspView(viewName);
        foundjspview.render(Map.of(), request, response);

        Mockito.verify(dummyDispatcher, times(1))
                .forward(eq(request), eq(response));
    }
}
