package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @DisplayName("handler에 맞는 HandlerAdapter인 HandlerExecutionHandlerAdapter을 반환한다.")
    @Test
    void getHandlerAdapter_ReturnHandlerExecutionHandlerAdapter() throws ServletException {
        // given
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        HandlerExecution handler = Mockito.mock(HandlerExecution.class);

        // when
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @DisplayName("handler에 맞는 HandlerAdapter인 ControllerHandlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapter_ReturnControllerHandlerAdapter() throws ServletException {
        // given
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        Controller handler = Mockito.mock(Controller.class);

        // when
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
    }
}
