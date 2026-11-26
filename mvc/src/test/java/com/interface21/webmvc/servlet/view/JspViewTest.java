package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class JspViewTest {

    @Test
    @DisplayName("redirect: 접두사를 제외한 경로로 리다이렉트하고 JSP로 forward하지 않는다")
    void redirectToTargetPathWithoutForwarding() throws Exception {
        final var view = new JspView("redirect:/annotation-test");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/annotation-test");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
