package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import nextstep.mvc.controller.tobe.fixture.AnnotationController;
import nextstep.mvc.controller.tobe.fixture.FakeHandlerAdapter;
import nextstep.mvc.controller.tobe.fixture.ImplementedController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @DisplayName("컨트롤러 상속 인스턴스에 대응하는 어댑터 반환")
    @Test
    void getHandler_controllerHandlerAdapter() {
        final var registry = new HandlerAdapterRegistry();
        registry.add(new ControllerHandlerAdapter());
        registry.add(new HandlerExecutionHandlerAdapter());

        final var controller = new ImplementedController();
        final var handlerAdapter = registry.getHandlerAdapter(controller);

        assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @DisplayName("컨트롤러 어노테이션 인스턴스에 대응하는 어댑터 반환")
    @Test
    void getHandler_handlerExecutionHandlerAdapter() throws NoSuchMethodException {
        final var registry = new HandlerAdapterRegistry();
        registry.add(new ControllerHandlerAdapter());
        registry.add(new HandlerExecutionHandlerAdapter());

        final var controller = new AnnotationController();
        final var execution = new HandlerExecution(controller, AnnotationController.class.getDeclaredMethod("get"));
        final var handlerAdapter = registry.getHandlerAdapter(execution);

        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @DisplayName("대응하는 어댑터가 없다면 예외 발생")
    @Test
    void getHandlerAdapter_throws_noSuchElementException() {
        final var registry = new HandlerAdapterRegistry();
        registry.add(new ControllerHandlerAdapter());
        registry.add(new HandlerExecutionHandlerAdapter());

        final var unsupported = new FakeHandlerAdapter();
        assertThatThrownBy(() -> registry.getHandlerAdapter(unsupported))
                .isInstanceOf(NoSuchElementException.class);
    }
}
