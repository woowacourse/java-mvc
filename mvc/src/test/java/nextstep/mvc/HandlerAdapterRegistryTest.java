package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러 어댑터를 추가한다.")
    void addHandlerAdapter() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        assertThatCode(() -> handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("핸들러에 따라서 적절한 어댑터를 찾는다.")
    void getHandlerAdapter() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());

        final HandlerExecution handlerExecution = new HandlerExecution(null, null);

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(HandlerExecutionAdapter.class);
    }
}
