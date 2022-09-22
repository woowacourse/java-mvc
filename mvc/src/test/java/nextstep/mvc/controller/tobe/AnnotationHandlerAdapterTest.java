package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;

class AnnotationHandlerAdapterTest {

    @DisplayName("처리할 수 있는 핸들러일 경우 True를 반환한다.")
    @Test
    void isSupportedHandler() {
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        boolean expected = handlerAdapter.supports(handlerExecution);
        assertThat(expected).isTrue();
    }

    @DisplayName("처리할 수 없는 핸들러일 경우 False를 반환한다.")
    @Test
    void isNotSupportedHandler() {
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        Controller controller = mock(Controller.class);

        boolean expected = handlerAdapter.supports(controller);
        assertThat(expected).isFalse();
    }
}
