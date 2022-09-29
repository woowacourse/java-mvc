package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerExecutionAdapter;
import nextstep.mvc.exception.NotFoundHandlerAdapterException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Nested
    class GetAdapter {

        @Test
        void getAdapter_메서드는_주어진_핸들러에_맞는_어댑터를_반환한다() {
            // given
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            final HandlerExecution handlerExecution = mock(HandlerExecution.class);

            handlerAdapterRegistry.register(new HandlerExecutionAdapter());

            // when
            final HandlerAdapter adapter = handlerAdapterRegistry.getAdapter(handlerExecution);

            // then
            assertThat(adapter).isInstanceOf(HandlerExecutionAdapter.class);
        }

        @Test
        void 주어진_핸들러에_맞는_어댑터가_없는_경우_예외를_던진다() {
            // given
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            final HandlerExecution handlerExecution = mock(HandlerExecution.class);

            // when & then
            assertThatThrownBy(() -> handlerAdapterRegistry.getAdapter(handlerExecution))
                    .isInstanceOf(NotFoundHandlerAdapterException.class);
        }
    }
}
