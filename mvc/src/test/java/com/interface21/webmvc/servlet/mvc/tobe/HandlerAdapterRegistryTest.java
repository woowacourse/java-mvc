package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    class LegacyController implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) {
            return null;
        }
    }

    @DisplayName("레거시 MVC 컨트롤러를 실행할 수 있는 핸들러 어댑터를 조회한다.")
    @Test
    void getManualHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new LegacyController());

        assertThat(handlerAdapter).isExactlyInstanceOf(ManualHandlerAdapter.class);
    }

    @DisplayName("어노테이션 기반 컨트롤러를 실행할 수 있는 핸들러 어댑터를 조회한다.")
    @Test
    void getAnnotationHandlerAdapter() {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        HandlerExecution handler = annotationHandlerMapping.getHandler(request);

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isExactlyInstanceOf(AnnotationHandlerAdapter.class);
    }
}
