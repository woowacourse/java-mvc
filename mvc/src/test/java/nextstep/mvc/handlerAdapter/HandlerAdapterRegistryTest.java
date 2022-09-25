package nextstep.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import samples.TestController;
import samples.TestHandlerAdapter;

class HandlerAdapterRegistryTest {

    @Test
    void getHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new TestHandlerAdapter());

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new TestController());

        assertThat(handlerAdapter).isInstanceOf(TestHandlerAdapter.class);
    }

    @Test
    void cantGetHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        assertThatThrownBy(
            () -> handlerAdapterRegistry.getHandlerAdapter(new TestController()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("해당 핸들러를 처리할 수 있는 핸들러 어댑터를 찾지 못했습니다.");
    }
}
