package nextstep.mvc.handeradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.mvc.common.exception.NotFoundHandlerAdapterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @DisplayName("HandlerAdapter 를 추가하고 핸들러의 HandlerAdapter 를 찾는다.")
    @Test
    void addAndGetHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new TestHandlerAdapter());

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new TestHandler());

        assertThat(handlerAdapter).isOfAnyClassIn(TestHandlerAdapter.class);
    }

    @DisplayName("HandlerAdapter 를 찾지 못하면 예외가 발생한다.")
    @Test
    void getInvalidHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new TestHandlerAdapter());

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(new Object()))
                .isInstanceOf(NotFoundHandlerAdapterException.class)
                .hasMessage("핸들러 어뎁터를 찾을 수 없습니다.");
    }
}
