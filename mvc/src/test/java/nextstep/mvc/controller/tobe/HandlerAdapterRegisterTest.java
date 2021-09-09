package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import nextstep.mvc.HandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.ManualController;
import samples.TestController;

class HandlerAdapterRegisterTest {

    private HandlerAdapterRegister handlerAdapterRegister;

    @BeforeEach
    void setUp() {
        handlerAdapterRegister = new HandlerAdapterRegister();
        handlerAdapterRegister.addHandlerAdapter(new AnnotationAdapter());
    }

    @Test
    void getHandlerAdapter() {
        Method method = TestController.class.getDeclaredMethods()[0];
        final Object handler = new HandlerExecution(new TestController(), method);

        final HandlerAdapter handlerAdapter = handlerAdapterRegister.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isNotNull();
    }

    @Test
    void getHandlerAdapterIncorrectHandler() {
        final Object handler = new ManualController();

        assertThatThrownBy(() -> handlerAdapterRegister.getHandlerAdapter(handler))
                .isInstanceOf(IllegalStateException.class);
    }
}
