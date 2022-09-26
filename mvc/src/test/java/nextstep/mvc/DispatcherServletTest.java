package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

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

    @DisplayName("json view를 응답하는 handler를 동작시킬 수 있다.")
    @Test
    void service_jsonResponse() throws IOException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getAttribute("name")).thenReturn("forky");
        when(request.getAttribute("age")).thenReturn(26);
        when(request.getRequestURI()).thenReturn("/json-test");
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter())
                .thenReturn(printWriter);

        assertThatNoException()
                .isThrownBy(() -> dispatcherServlet.service(request, response));
    }

    @DisplayName("Interface 기반의 handler를 동작시킬 수 있다.")
    @Test
    void service_interfaceHandler() {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new TestManualHandlerMapping());
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
