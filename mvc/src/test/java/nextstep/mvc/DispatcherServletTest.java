package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ManualHandlerMapping;

class DispatcherServletTest {

    @DisplayName("Annotation 기반의 handler를 동작시킬 수 있다.")
    @Test
    void service_annotationHandler() {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(""))
                .thenReturn(requestDispatcher);

        assertThatNoException()
                .isThrownBy(() -> dispatcherServlet.service(request, response));
    }

    @DisplayName("Interface 기반의 handler를 동작시킬 수 있다.")
    @Test
    void service_interfaceHandler() {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        assertThatNoException()
                .isThrownBy(() -> dispatcherServlet.service(request, response));
    }

}
