package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.ManualTestHandlerMapping;

class HandlerMappingRegistryTest {

    @ParameterizedTest
    @MethodSource("getHandlerData")
    @DisplayName("getHandler 메소드는 요청에 매핑되는 핸들러를 반환한다.")
    void getHandler(String requestUri, Class<?> handler) {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.addHandlerMapping(new ManualTestHandlerMapping());
        handlerMappingRegistry.init();

        // when
        final Optional<Object> handlerMappingResult = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handlerMappingResult.get()).isInstanceOf(handler);
    }

    public static Stream<Arguments> getHandlerData() {
        return Stream.of(
                Arguments.of("/get-test", HandlerExecution.class),
                Arguments.of("/manual-test", Controller.class)
        );
    }
}