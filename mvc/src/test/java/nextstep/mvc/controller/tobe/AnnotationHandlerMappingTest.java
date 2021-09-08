package nextstep.mvc.controller.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution.getHandler()).isExactlyInstanceOf(TestController.class);
    }

    @Test
    void post() throws Exception {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        // when
        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution.getHandler()).isExactlyInstanceOf(TestController.class);
    }
}
