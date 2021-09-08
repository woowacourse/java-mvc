package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
    }

    @DisplayName("getAdapter 요청시")
    @Nested
    class GetAdapter {

        private HandlerExecution handlerExecution;

        @BeforeEach
        void setUp() {
            handlerExecution = mock(HandlerExecution.class);
        }

        @DisplayName("핸들링을 지원하는 Adapter를 찾으면 반환한다.")
        @Test
        void getAdapter() {
            // given
            AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
            handlerAdapters.add(handlerAdapter);

            // when, then
            assertThat(handlerAdapters.getAdapter(handlerExecution)).isEqualTo(handlerAdapter);
        }

        @DisplayName("핸들링을 지원하는 Adapter를 찾지 못하면 예외가 발생한다.")
        @Test
        void getAdapterException() {
            assertThatThrownBy(() -> handlerAdapters.getAdapter(handlerExecution))
                .isExactlyInstanceOf(NoSuchElementException.class);
        }
    }
}