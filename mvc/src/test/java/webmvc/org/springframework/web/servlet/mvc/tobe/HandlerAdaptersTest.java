package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

class HandlerAdaptersTest {
    private final HandlerAdapters handlerAdaptors = new HandlerAdapters();

    @DisplayName("HandlerAdapter들을 리스트에 올바르게 가져온다.")
    @Test
    void init() {
        // when & then
        assertAll(
                () -> assertThat(handlerAdaptors.getAdapters()).isEmpty(),
                handlerAdaptors::init,
                () -> assertThat(handlerAdaptors.getAdapters()).hasSize(2)
        );
    }

    @DisplayName("Adapters에서 HandlerExecution을 찾아 반환한다.")
    @Test
    void findAdapter_HandlerExecution() throws ServletException {
        // given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        handlerAdaptors.init();

        //when
        final HandlerAdapter handlerAdaptor = handlerAdaptors.findAdapter(handlerExecution);

        //then
        assertThat(handlerAdaptor).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("Adapters에서 Controller를 찾아 반환한다.")
    @Test
    void findAdapter_Controller() throws ServletException {
        // given
        final Controller controller = mock(Controller.class);
        handlerAdaptors.init();

        //when
        final HandlerAdapter handlerAdaptor = handlerAdaptors.findAdapter(controller);

        //then
        assertThat(handlerAdaptor).isInstanceOf(ManualHandlerAdapter.class);
    }

    @DisplayName("Adapters에서 원하는 타입을 찾지 못하면 null을 반환한다.")
    @Test
    void findAdapter_NotFoundObject() throws ServletException {
        // given
        final Object object = mock(Object.class);
        handlerAdaptors.init();

        //when & then
        assertThat(handlerAdaptors.findAdapter(object)).isNull();
    }
}
