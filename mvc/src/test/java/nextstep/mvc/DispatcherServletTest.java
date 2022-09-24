package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.mvc.controller.tobe.exception.NotSupportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);
        dispatcherServlet.addHandlerAdapter(handlerExecutionHandlerAdapter);
        dispatcherServlet.init();
    }

    @DisplayName("Controller 인터페이스를 상속한 핸들러 뿐 아니라 어노테이션 기반의 핸들러를 지원한다.")
    @Test
    void getHandler() throws ServletException, IOException {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(""))
                .thenReturn(requestDispatcher);
        when(request.getAttribute("id")).thenReturn("dwoo");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("지원하지 않는 핸들러에 대해서는 예외가 발생한다.")
    @Test
    void invalidRequest() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-invalid");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(NotSupportHandler.class);
    }
}
