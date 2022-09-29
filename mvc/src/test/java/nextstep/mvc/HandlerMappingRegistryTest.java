package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Test
    void Handler를_찾아_반환한다() {
        // given
        final var handler = new Object();
        final var handlerMapping = mock(HandlerMapping.class);
        handlerMappingRegistry.add(handlerMapping);
        given(handlerMapping.getHandler(any()))
                .willReturn(handler);

        // when
        final var actual = handlerMappingRegistry.getHandler(any());

        // then
        assertAll(
                () -> verify(handlerMapping).getHandler(any()),
                () -> assertThat(handler).isEqualTo(actual)
        );
    }

    @Test
    void 요청을_처리할_Handler가_없다면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(null))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("요청을 처리할 핸들러를 찾을 수 없습니다.");
    }
}
