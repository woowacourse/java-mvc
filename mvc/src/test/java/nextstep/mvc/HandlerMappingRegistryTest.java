package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.exception.NoSuchHandlerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @DisplayName("handlerMapping을 추가한다.")
    @Test
    void handlerMapping을_추가한다() {
        // given
        HandlerMapping handlerMapping = new AnnotationHandlerMapping("tobe");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/mock");
        when(request.getMethod()).thenReturn("GET");

        // when
        handlerMappingRegistry.add(handlerMapping);

        // then
        assertDoesNotThrow(() -> handlerMapping.getHandler(request));
    }

    @DisplayName("핸들러가 존재하지 않을 경우 예외를 던진다.")
    @Test
    void 핸들러가_존재하지_않을_경우_예외를_던진다() {
        // given
        HandlerMapping handlerMapping = new AnnotationHandlerMapping("tobe");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/mock");
        when(request.getMethod()).thenReturn("POST");

        // when
        handlerMappingRegistry.add(handlerMapping);

        // then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoSuchHandlerException.class);
    }
}
