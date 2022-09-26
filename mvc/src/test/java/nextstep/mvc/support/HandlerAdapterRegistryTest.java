package nextstep.mvc.support;

import nextstep.mvc.controller.tobe.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {

    @Test
    void 핸들러를_지원하는_어뎁터를_불러온다() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        // when
        final HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(mock(HandlerExecution.class));

        // then
        assertThat(actual).isInstanceOf(HandlerExecutionAdapter.class);
    }

    @Test
    void 지원하는_어뎁터가_없다면_예외를_발생한다() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        // when, then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(mock(HandlerExecution.class)))
                .isInstanceOf(HandlerAdapterNotFoundException.class);
    }

}
