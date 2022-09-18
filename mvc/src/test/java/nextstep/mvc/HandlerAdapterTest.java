package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterTest {

    @Test
    void Handler가_Controller이면_ControllerHandlerAdapter는_support한다() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final boolean expected = true;

        // when
        final boolean actual = handlerAdapter.supports((Controller) (req, res) -> null);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void Handler가_HandlerExecution이면_HandlerExecutionAdpater는_support한다() throws NoSuchMethodException {
        // given
        final HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
        final Class<?> target = TestController.class;
        final Method method = target.getDeclaredMethod("put", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handler = new HandlerExecution(target, method);
        final boolean expected = true;

        // when
        final boolean actual = handlerAdapter.supports(handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
