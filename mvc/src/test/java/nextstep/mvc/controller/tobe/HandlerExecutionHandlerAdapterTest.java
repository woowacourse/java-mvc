package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestObject;

class HandlerExecutionHandlerAdapterTest {

    @Test
    void HandlerExecutionAdapter는_HandlerExecution_클래스의_객체를_처리할_수_있다() throws NoSuchMethodException {
        // given
        final Method executionMethod = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(TestController.class, executionMethod);

        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        // when
        final boolean result = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void HandlerExecutionAdapter는_HandlerExecution_클래스의_객체가_아니면_처리할_수_없다() {
        // given
        final TestObject testObject = new TestObject("/123.html", "테스트 객체");

        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        // when
        final boolean result = handlerAdapter.supports(testObject);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void Handler를_처리하면_modelAndView를_반환한다() throws Exception {
        // given
        final Method executionMethod = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(new TestController(), executionMethod);

        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        // then
        AssertionsForClassTypes.assertThat(result).extracting("view", "model")
                .containsExactly(new JspView(""), Map.of("id", "gugu"));
    }
}
