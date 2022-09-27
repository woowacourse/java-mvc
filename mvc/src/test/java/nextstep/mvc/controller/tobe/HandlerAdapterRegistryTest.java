package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import nextstep.mvc.HandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerAdapterRegistryTest {

    @DisplayName("지원하는 핸들러 어댑터를 반환한다.")
    @Test
    void getHandler() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final AnnotationHandlerAdapter handlerAdapter = mock(AnnotationHandlerAdapter.class);
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);

        final Object handler = mock(HandlerExecution.class);

        given(handlerAdapter.supports(handler))
                .willReturn(true);

        // when
        final HandlerAdapter expected = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isEqualTo(expected);
    }
}
