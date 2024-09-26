package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompositeHandlerMappingTest {

    private CompositeHandlerMapping compositeHandlerMapping;

    @BeforeEach
    void init() {
        HandlerMapping manualHandlerMapping = request -> {
            if ("/index.jsp".equals(request.getRequestURI())) {
                return new ForwardController("/index.jsp");
            }
            return null;
        };

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();

        this.compositeHandlerMapping = new CompositeHandlerMapping(
                List.of(manualHandlerMapping, annotationHandlerMapping)
        );
    }

    @Test
    void getHandlerFromManualHandlerMapping() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/index.jsp");

        Object handler = compositeHandlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(Controller.class);
    }

    @Test
    void getHandlerFromAnnotationHandlerMapping() {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");

        Object handler = compositeHandlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
