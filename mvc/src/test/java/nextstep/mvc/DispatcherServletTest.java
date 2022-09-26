package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdaptor;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;

public class DispatcherServletTest {

    @Test
    @DisplayName("service를 올바르게 수행한다.")
    void doService() throws ServletException {

        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet(new HandlerMappingRegistry(), new HandlerAdaptorRegistry());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addHandlerAdaptors(new AnnotationHandlerAdaptor());
        dispatcherServlet.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getAttribute(any())).thenReturn("panda");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertAll(
            () -> verify(request).getRequestDispatcher(any()),
            () -> verify(requestDispatcher).forward(request, response)
        );
    }
}
