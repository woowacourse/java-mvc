package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import samples.ManualTestHandlerAdapter;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.ManualTestController;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("getHandlerAdapter 메소드는")
    class GetHandlerAdapter {

        @ParameterizedTest
        @MethodSource("getHandlerAdapterData")
        @DisplayName("입력 받은 핸들러에 매핑되는 핸들러 어댑터를 반환한다.")
        void getHandlerAdapter(Object handler, Class<?> handlerAdapter) {
            // given
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
            handlerAdapterRegistry.addHandlerAdapter(new ManualTestHandlerAdapter());

            // when
            final HandlerAdapter result = handlerAdapterRegistry.getHandlerAdapter(handler);

            // then
            assertThat(result).isInstanceOf(handlerAdapter);
        }


        private Stream<Arguments> getHandlerAdapterData() throws NoSuchMethodException {
            return Stream.of(
                    Arguments.of(getHandlerExecution(), HandlerExecutionHandlerAdapter.class),
                    Arguments.of(new ManualTestController(), ManualTestHandlerAdapter.class)
            );
        }

        private HandlerExecution getHandlerExecution() throws NoSuchMethodException {
            final TestController testController = new TestController();
            final Method method = testController.getClass()
                    .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

            return new HandlerExecution(testController, method);
        }

        @Test
        @DisplayName("매핑되는 핸들러 어댑터가 없다면 예외를 발생시킨다.")
        void exception_notFoundHandlerAdapter() {
            // given
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

            // when & then
            assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(new TestController()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("HandlerAdapter not found");
        }
    }
}