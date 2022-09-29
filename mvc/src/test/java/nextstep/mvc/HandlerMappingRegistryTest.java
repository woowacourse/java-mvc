package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("Registry에 HandlerMapping을 추가한다.")
    @Test
    void addHandlerAdapter() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        // when, then
        assertThatCode(() -> handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples")))
                .doesNotThrowAnyException();
    }


    @DisplayName("Registry로부터 HandlerMapping를 반환받는다.")
    @Test
    void getHandlerAdapter() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        // when
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        final Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
