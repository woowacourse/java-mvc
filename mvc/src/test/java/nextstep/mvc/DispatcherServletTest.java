package nextstep.mvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
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

    @DisplayName("Annotation 기반 핸드러가 정상 작동하는지 확인한다.")
    @Test
    void getHandler() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(""))
                .thenReturn(requestDispatcher);
        when(request.getAttribute("id")).thenReturn("어썸오_좋은_하루_보내세요.");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
    }


    @DisplayName("지원하지 않는 핸들러에 대해서는 예외가 발생한다.")
    @Test
    void invalidRequest() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-invalid");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
