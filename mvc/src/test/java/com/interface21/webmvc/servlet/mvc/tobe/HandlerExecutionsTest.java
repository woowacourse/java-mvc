package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.possible.TestController;

class HandlerExecutionsTest {

    @DisplayName("중복 핸들러 키를 등록할 경우 예외가 발생한다.")
    @Test
    void exceptionWithDuplication() throws NoSuchMethodException {
        // given
        Method getMethod = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);

        HandlerExecutions executions = new HandlerExecutions();

        // when
        executions.addHandlerExecution(TestController.class, getMethod);
        assertThatThrownBy(() -> executions.addHandlerExecution(TestController.class, getMethod))
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }
}
