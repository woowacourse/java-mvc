package nextstep.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.adapters.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.mappings.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.resolvers.DefaultResolver;
import nextstep.mvc.controller.tobe.resolvers.JspResolver;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void service() throws ServletException, IOException {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        servlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        servlet.addModelAndViewResolver(new DefaultResolver());
        servlet.addModelAndViewResolver(new JspResolver());

        servlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute(any())).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));

        // when
        servlet.service(request, response);

        // then
        verify(request.getRequestDispatcher(any())).forward(any(), any());
    }
}
