package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DispatcherServletTest {

    @Test
    void 요청을_처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(mock(HandlerMapping.class));
        dispatcherServlet.init();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
