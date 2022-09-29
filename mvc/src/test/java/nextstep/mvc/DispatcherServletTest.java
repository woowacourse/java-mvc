package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void 요청을_처리할_수_없을_때_NOT_FOUND_페이지를_내보낸다() throws IOException {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappingRegistry,
                handlerAdapterRegistry);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // when
        when(req.getRequestURI()).thenReturn("/login");
        when(req.getMethod()).thenReturn("POST");

        dispatcherServlet.service(req, res);

        // then
        verify(res).sendRedirect("/404.jsp");
    }

    @Test
    void 예외가_발생하면_ERROR_페이지를_내보낸다() throws IOException {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappingRegistry,
                handlerAdapterRegistry);
        dispatcherServlet.init();

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // when
        when(req.getRequestURI()).thenReturn("/get-test");
        when(req.getMethod()).thenReturn("GET");

        dispatcherServlet.service(req, res);

        // then
        verify(res).sendRedirect("/500.jsp");
    }
}
