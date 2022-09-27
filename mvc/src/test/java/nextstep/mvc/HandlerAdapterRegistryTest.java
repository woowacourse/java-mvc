package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.adapter.AnnotationHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setup() {
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @DisplayName("어노테이션 핸들러 어댑터를 찾을 수 있다.")
    @Test
    void findAdapter_Annotation() {
        //given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        //when
        HandlerAdapter adapter = handlerAdapterRegistry.findAdapter(handlerExecution);

        //then
        assertThat(adapter.getClass()).isEqualTo(AnnotationHandlerAdapter.class);
    }

    @DisplayName("일치하는 핸들러가 없을 시 예외가 발생한다.")
    @Test
    void findAdapter_Exception() {
        //given
        Integer handler = 0;

        //when then
        assertThatThrownBy(() -> handlerAdapterRegistry.findAdapter(handler))
                .isInstanceOf(NoSuchElementException.class);
    }
}
