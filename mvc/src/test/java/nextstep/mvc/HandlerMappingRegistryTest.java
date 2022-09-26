package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("핸들러 매핑을 추가한다.")
    void addHandlerMapping() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        assertThatCode(() -> handlerMappingRegistry.addHandlerMapping(
                new AnnotationHandlerMapping("samples"))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("핸들러 매핑을 초기화한다.")
    void initialize() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        assertThatCode(handlerMappingRegistry::initialize)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("핸들러 매핑을 가져온다.")
    void getHandler() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler.orElseThrow()).isInstanceOf(HandlerExecution.class);
    }
}
