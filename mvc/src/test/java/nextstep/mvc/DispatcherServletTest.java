package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

class DispatcherServletTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void service_jsonResponse() throws IOException, ServletException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        final String name = "forky";
        final int age = 26;
        final TestUser user = new TestUser("forky", 26);

        when(request.getAttribute("name")).thenReturn(name);
        when(request.getAttribute("age")).thenReturn(age);
        when(request.getRequestURI()).thenReturn("/json-test");
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter())
                .thenReturn(printWriter);

        dispatcherServlet.service(request, response);
        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(user));
    }
}
