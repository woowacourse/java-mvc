package nextstep.mvc.adpater.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.adpater.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("핸들러가 처리할 수 객체인지 판단한다.")
    void supports() throws NoSuchMethodException {
        // given
        final HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        // when
        final boolean actual = annotationHandlerAdapter.supports(handlerExecution);

        // then
        assertTrue(actual);
    }

    @Test
    @DisplayName("요청을 처리하고 결과를 반환한다.")
    void handle() throws Exception {
        // given
        final HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        final ModelAndView modelAndView = annotationHandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
