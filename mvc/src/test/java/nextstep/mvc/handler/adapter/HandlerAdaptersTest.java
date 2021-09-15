package nextstep.mvc.handler.adapter;

import nextstep.mvc.controller.ControllerScanner;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("HandlerAdapters 테스트")
class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @DisplayName("handler를 지원하는 handlerAdapter를 찾아준다 - 성공")
    @Test
    void getHandlerAdapter() {
        // given
        final HandlerExecution handler = mock(HandlerExecution.class);

        // when
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("handler를 지원하는 handlerAdapter를 찾아준다 - 실패 - 존재하지 않음")
    @Test
    void getHandlerAdapterFailureWhenNotExists() {
        // given
        // when
        // then
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(new ControllerScanner()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}