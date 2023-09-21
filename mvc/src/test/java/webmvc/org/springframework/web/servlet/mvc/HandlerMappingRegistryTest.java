package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

class HandlerMappingRegistryTest {

    @Test
    void 적절한_handler를_찾지_못하면_예외가_발생한다() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() -> handlerMappingRegistry.findHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "요청을 처리할 수 있는 Handler를 찾을 수 없습니다!: "
                                + request.getMethod() + " "
                                + request.getRequestURI()
                );
    }

    @Test
    void 적절한_handler를_반환한다() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final Object handlerMapping = handlerMappingRegistry.findHandler(request);

        assertThat(handlerMapping).isInstanceOf(HandlerExecution.class);
    }
}
