package webmvc.org.springframework.web.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;
    private HandlerMapping annotationHandlerMapping;
    private HandlerMapping handlerMapping;
    private HttpServletRequest request;


    @BeforeEach
    void setUp() {
        annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMapping = mock(HandlerMapping.class);

        List<HandlerMapping> handlerMappings = new ArrayList<>(List.of(
                annotationHandlerMapping
        ));

        registry = new HandlerMappingRegistry(handlerMappings);

        request = mock(HttpServletRequest.class);

    }

    @Test
    void initialize() {
        // when
        registry.initialize();

        // then
        verify(annotationHandlerMapping, times(1)).initialize();
    }

    @Test
    void addHandlerMappings() {
        // when
        registry.addHandlerMappings(handlerMapping);
        registry.initialize();

        // then
        verify(handlerMapping, times(1)).initialize();
    }

    @Test
    void getHandler_success() {
        // given
        Object handler = new Object();
        when(annotationHandlerMapping.getHandler(request)).thenReturn(handler);

        // when
        Optional<Object> result = registry.getHandler(request);

        assertThat(result).isNotEmpty()
                .contains(handler);
    }

    @Test
    void getHandler_fail() {
        when(annotationHandlerMapping.getHandler(request)).thenReturn(null);

        // when
        Optional<Object> result = registry.getHandler(request);

        assertThat(result).isEmpty();
    }
}
