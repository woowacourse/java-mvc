package nextstep.mvc;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdaptor;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;

class DispatcherServletTest {

    private static final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeAll
    static void setUp() {
        dispatcherServlet.addHandlerAdaptor(new AnnotationHandlerAdaptor());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();
    }

    @Test
    @DisplayName("service 수행을 검증한다.")
    void service() throws ServletException, IOException {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(request, times(1)).getRequestDispatcher("");
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
