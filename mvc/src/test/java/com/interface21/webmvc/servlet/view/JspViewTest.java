package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    @DisplayName("JSP 뷰는 모델 데이터를 요청에 담아 JSP로 전달한다")
    void renderForwardsModelToJsp() throws Exception {
        // given
        final var view = new JspView("/get-test.jsp");
        final var model = Map.of("id", "gugu");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/get-test.jsp")).thenReturn(dispatcher);

        // when
        view.render(model, request, response);

        // then
        final var inOrder = inOrder(request, dispatcher);
        inOrder.verify(request).setAttribute("id", "gugu");
        inOrder.verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("리다이렉트 뷰는 지정한 경로로 이동시킨다")
    void renderRedirectsToTargetPath() throws Exception {
        // given
        final var view = new JspView("redirect:/login.jsp");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        view.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/login.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
