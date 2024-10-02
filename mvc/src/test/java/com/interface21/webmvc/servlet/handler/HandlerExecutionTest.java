package com.interface21.webmvc.servlet.handler;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.NoArgsController;

class HandlerExecutionTest {

    @Test
    @DisplayName("컨트롤러의 메서드가 요구하는 파라미터를 동적으로 대응하여 의존성을 주입한다.")
    void passArgumentsWithRequiredParameters() throws Exception {
        // given
        Method method = NoArgsController.class.getMethod("noArgsMethod");
        HandlerExecution handlerExecution = new HandlerExecution(method, NoArgsController.class);

        // when & then
        assertThatCode(() -> handlerExecution.handle(null, null))
                .doesNotThrowAnyException();
    }
}
