package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.mvc.exception.NotFoundHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void set(){
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Test
    @DisplayName("핸들러 어댑터를 등록한다.")
    void addHandlerAdapter() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isEqualTo(handlerAdapter);
    }

    @Test
    @DisplayName("찾을 수 없는 핸들러 어뎁터라면 예외를 반환한다.")
    void addHandlerAdapter_notFound() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                        .isInstanceOf(NotFoundHandlerAdapter.class);
    }

}
