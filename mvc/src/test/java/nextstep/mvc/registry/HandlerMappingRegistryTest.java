package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.exception.HandlerNotFoundException;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 핸들러를_레지스트리에_추가한다() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HttpServletRequest request = mock(HttpServletRequest.class);

        System.out.println(getClass().getPackage());
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("nextstep.mvc");
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
        handlerMappingRegistry.init();

        when(request.getRequestURI()).thenReturn("/dummy");
        when(request.getMethod()).thenReturn("GET");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isNotNull();
    }

    @Test
    void 처리할_수_있는_핸들러가_없으면_예외를_반환한다() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/not-mapped");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isExactlyInstanceOf(HandlerNotFoundException.class);
    }
}
