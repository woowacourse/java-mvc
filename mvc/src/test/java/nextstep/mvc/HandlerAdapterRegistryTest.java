package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 핸들러어댑터가_존재할_경우_반환한다() {
        // given
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("slow");

        final Object handler = handlerMapping.getHandler(request);

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        // when
        final HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(actual).isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @Test
    void 핸들러가어댑터_존재하지_않는경우_예외를_발생한다() {
        // given
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getAttribute("id")).thenReturn("slow");

        final Object handler = handlerMapping.getHandler(request);

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        // then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다.");
    }
}
