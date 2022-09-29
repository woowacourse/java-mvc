package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("HandlerAdapter를 등록하고 adapter를 꺼내온다.")
    void getAdaptor() {
        // given
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();

        final TestAnnotationController controller = new TestAnnotationController();
        final Method method = controller.getClass().getMethods()[0];
        final HandlerExecution handler = new HandlerExecution(controller, method);

        // when
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());

        final HandlerAdapter actual = registry.getAdaptor(handler);

        // then
        assertThat(actual).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
