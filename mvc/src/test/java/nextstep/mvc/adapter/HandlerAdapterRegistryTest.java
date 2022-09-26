package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestImplementController;

class HandlerAdapterRegistryTest {

    @DisplayName("handler에 알맞은 adapter를 찾는다.")
    @Test
    void getAdapter() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.init();

        final TestImplementController handler = new TestImplementController();
        assertThat(handlerAdapterRegistry.getAdapter(handler)).isInstanceOf(ControllerAdapter.class);
    }
}
