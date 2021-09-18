package nextstep.mvc.adapter;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.exception.MvcException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerAdaptersTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new HandlerExecutionAdapter();
    }

    @DisplayName("핸들러 어댑터를 찾는다.")
    @Test
    void findAdapter() throws NoSuchMethodException {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerAdapters handlerAdapters = new HandlerAdapters(Collections.singletonList(handlerAdapter));

        // when, then
        assertThat(handlerAdapters.findAdapter(handlerExecution))
                .isEqualTo(handlerAdapter);
    }

    @DisplayName("핸들러 어댑터를 찾는데 실패한다.")
    @Test
    void findHandlerWhenNonSupports() {
        // given
        UndefinedHandler undefinedHandler = mock(UndefinedHandler.class);
        HandlerAdapters handlerAdapters = new HandlerAdapters(Collections.singletonList(handlerAdapter));

        // when, then
        assertThatThrownBy(() -> handlerAdapters.findAdapter(undefinedHandler))
                .isInstanceOf(MvcException.class)
                .hasMessageContaining("핸들러를 처리하는 어댑터가 없습니다");
    }

    private static class UndefinedHandler {

    }
}
