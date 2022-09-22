package nextstep.mvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdaptor;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("Registry에서 AnnotationHandlerAdapter를 조회한다.")
    void getAnnotationHandlerAdapter() {
        // given
        final HandlerAdapterRegistry adapterRegistry = new HandlerAdapterRegistry();
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();

        adapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        adapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdaptor());

        // when
        final Object handler = annotationHandlerMapping.getHandler(request);
        final HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdaptor.class);
    }

    @Test
    @DisplayName("Registry에서 ControllerHandlerAdapter를 조회한다.")
    void getControllerHandlerAdapter() {
        // given
        final HandlerAdapterRegistry adapterRegistry = new HandlerAdapterRegistry();

        adapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        adapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdaptor());

        // when
        final Object handler = new ForwardController("/");
        final HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
    }
}
