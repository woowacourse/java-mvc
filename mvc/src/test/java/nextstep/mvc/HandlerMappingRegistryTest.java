package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.exception.NotFoundHandlerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry;

    public HandlerMappingRegistryTest() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Test
    void initialize_메서드는_입력받은_HandlerMapping을_초기화한다() {
        // given
        final AnnotationHandlerMapping handlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMappingRegistry.register(handlerMapping);

        // when
        handlerMappingRegistry.initialize();

        // then
        verify(handlerMapping).initialize();

    }

    @Nested
    @DisplayName("getHandler 메서드는")
    class GetHandler {

        @Test
        void 요청을_처리할_수_있는_핸들러를_반환한다() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final AnnotationHandlerMapping handlerMapping = mock(AnnotationHandlerMapping.class);
            final HandlerExecution handlerExecution = mock(HandlerExecution.class);

            when(handlerMapping.getHandler(request)).thenReturn(handlerExecution);

            handlerMappingRegistry.register(handlerMapping);

            // when
            final Object handler = handlerMappingRegistry.getHandler(request);

            // then
            assertThat(handler).isSameAs(handlerExecution);
        }

        @Test
        void 요청을_처리할_수_있는_핸들러가_존재하지_않을_때_예외를_던진다() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);

            // when & then
            Assertions.assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                    .isInstanceOf(NotFoundHandlerException.class);
        }
    }

}
