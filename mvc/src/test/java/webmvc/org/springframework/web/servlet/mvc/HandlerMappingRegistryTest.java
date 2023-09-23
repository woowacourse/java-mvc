package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.util.NoSuchElementException;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("request 에 맞는 Handler 를 반환한다.")
    void matchHandler_handlerExecution() throws Exception {
        final var annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");
        final var handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
        final var handlerExecution = (HandlerExecution) handler;
        assertThat(handlerExecution.getMethodName()).isEqualTo("findUserId");
    }

    @Test
    @DisplayName("일치하지 않는 형태의 Handler 를 적용하려고 하면 예외가 발생한다.")
    void matchHandler_validate() {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/never-exists");

        assertThatThrownBy((() -> handlerMappingRegistry.getHandler(request)))
                .isInstanceOf(NoSuchElementException.class);
    }

}
