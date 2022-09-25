package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.support.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionAdapterTest {

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

    @Test
    void Handler를_동작시킨다() throws NoSuchMethodException {
        // given
        final HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Object handler = new TestController();
        final Method method = TestController.class
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // when
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, new HandlerExecution(handler, method));

        // then
        assertAll(
                () -> assertThat(modelAndView.getView()).usingRecursiveComparison()
                        .isEqualTo(new JspView("view")),
                () -> assertThat(modelAndView.getModel()).containsEntry("id", "gugu")
        );
    }
}
