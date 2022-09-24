package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.exception.NoSuchHandlerAdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @DisplayName("handlerAdapter를 추가한다.")
    @Test
    void handlerAdapter를_추가한다() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HandlerMapping handlerMapping = new AnnotationHandlerMapping("nextstep.mvc.controller.tobe");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/mock");
        when(request.getMethod()).thenReturn("GET");
        handlerMappingRegistry.add(handlerMapping);

        // when
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapterRegistry.add(handlerAdapter);
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertDoesNotThrow(() -> handlerAdapterRegistry.getHandlerAdapter(handler));
    }

    @DisplayName("핸들러 어뎁터가 존재하지 않는 경우 예외를 던진다.")
    @Test
    void 핸들러_어뎁터가_존재하지_않는_경우_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(null))
                .isInstanceOf(NoSuchHandlerAdapterException.class);
    }
}
