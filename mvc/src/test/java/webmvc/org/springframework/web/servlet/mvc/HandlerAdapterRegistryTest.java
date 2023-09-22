package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @DisplayName("handler에 맞는 HandlerAdapter인 AnnotationHandlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapter_ReturnAnnotationHandlerAdapter() throws ServletException {
        // given
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        HandlerExecution handler = Mockito.mock(HandlerExecution.class);

        // when
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("handler에 맞는 HandlerAdapter인 ManualHandlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapter_ReturnManualHandlerAdapter() throws ServletException {
        // given
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        Controller handler = Mockito.mock(Controller.class);

        // when
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(ManualHandlerAdapter.class);
    }
}
