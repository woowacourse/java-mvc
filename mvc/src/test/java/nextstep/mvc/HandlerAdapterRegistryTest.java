package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @DisplayName("Registry에 HandlerAdapter를 추가한다.")
    @Test
    void addHandlerAdapter() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        // when, then
        assertThatCode(() -> handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter()))
                .doesNotThrowAnyException();
    }


    @DisplayName("Registry로부터 HandlerAdapter를 반환받는다.")
    @Test
    void getHandlerAdapter() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        // when
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(mock(HandlerExecution.class));

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);

    }
}
