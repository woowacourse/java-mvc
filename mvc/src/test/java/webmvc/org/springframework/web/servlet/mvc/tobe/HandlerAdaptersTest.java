package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {
    private final HandlerAdapters handlerAdaptors = new HandlerAdapters();

    @DisplayName("HandlerAdapter들을 리스트에 올바르게 가져온다.")
    @Test
    void init() {
        // when & then
        assertAll(
                () -> assertThat(handlerAdaptors.getAdapters()).isEmpty(),
                () -> handlerAdaptors.add(new AnnotationHandlerAdapter()),
                () -> assertThat(handlerAdaptors.getAdapters()).hasSize(1)
        );
    }

    @DisplayName("Adapters에서 HandlerExecution을 찾아 반환한다.")
    @Test
    void findAdapter_HandlerExecution() throws ServletException {
        // given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        handlerAdaptors.add(new AnnotationHandlerAdapter());

        //when
        final HandlerAdapter handlerAdaptor = handlerAdaptors.findAdapter(handlerExecution);

        //then
        assertThat(handlerAdaptor).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("Adapters에서 원하는 타입을 찾지 못하면 예외 처리한다.")
    @Test
    void findAdapter_NotFoundObject() throws ServletException {
        // given
        final Object object = mock(Object.class);
        handlerAdaptors.add(new AnnotationHandlerAdapter());

        //when & then
        assertThatThrownBy(() -> handlerAdaptors.findAdapter(object))
                .isInstanceOf(ServletException.class)
                .hasMessage("No adapters support handler");
    }
}
