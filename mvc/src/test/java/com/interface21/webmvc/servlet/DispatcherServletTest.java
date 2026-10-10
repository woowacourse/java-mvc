package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMethodNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private final HandlerMappingRegistry handlerMappings = mock(HandlerMappingRegistry.class);
    private final HandlerExecutor handlerExecutor = mock(HandlerExecutor.class);
    private final DispatcherServlet servlet = new DispatcherServlet(handlerMappings, handlerExecutor);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void 지원하지_않는_메서드는_405와_지원하는_메서드_헤더로_응답한다() throws Exception {
        when(handlerMappings.getHandler(request)).thenThrow(new RequestMethodNotSupportedException(
                "PUT", Set.of(RequestMethod.POST, RequestMethod.GET)));

        servlet.service(request, response);

        final var responseOrder = inOrder(response);
        responseOrder.verify(response).setHeader("Allow", "GET, POST");
        responseOrder.verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        verifyNoInteractions(handlerExecutor);
    }

    @Test
    void 처리하지_못한_예외는_원인을_보존해서_전달한다() throws Exception {
        final Object handler = new Object();
        final IOException cause = new IOException("handler failed");
        when(handlerMappings.getHandler(request)).thenReturn(Optional.of(handler));
        when(handlerExecutor.execute(handler, request, response)).thenThrow(cause);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("handler failed")
                .hasCause(cause);
    }

    @Test
    void Error는_일반적인_요청_예외로_포장하지_않는다() {
        final AssertionError error = new AssertionError("unrecoverable failure");
        when(handlerMappings.getHandler(request)).thenThrow(error);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isSameAs(error);
    }
}
