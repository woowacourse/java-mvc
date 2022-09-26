package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Test
    void HandlerAdapter를_찾아_반환한다() {
        // given
        final var handler = new Object();
        final var handlerAdapter = mock(HandlerAdapter.class);
        handlerAdapterRegistry.add(handlerAdapter);
        given(handlerAdapter.supports(handler))
                .willReturn(true);

        // when
        final var actual = handlerAdapterRegistry.getAdapter(handler);

        // then
        assertAll(
                () -> verify(handlerAdapter).supports(handler),
                () -> assertThat(actual).isEqualTo(handlerAdapter)
        );
    }

    @Test
    void 지원하는_HandlerAdapter를_찾을_수_없으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> handlerAdapterRegistry.getAdapter(new Object()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("handlerAdapter 를 찾을 수 없습니다.");
    }
}
