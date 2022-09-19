package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @DisplayName("ManualHandlerAdapter가 처리할 수 있는 객체면 true를 반환한다.")
    @Test
    void returnTrueWhenAnnotationHandlerAdapterCanHandle() {
        ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        Controller controller = mock(Controller.class);

        boolean actual = handlerAdapter.supports(controller);

        assertThat(actual).isTrue();
    }

    @DisplayName("ManualHandlerAdapter가 처리할 수 없는 객체면 false를 반환한다.")
    @Test
    void returnFalseWhenAnnotationHandlerAdapterCannotHandle() {
        ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        boolean actual = handlerAdapter.supports(handlerExecution);

        assertThat(actual).isFalse();
    }
}
