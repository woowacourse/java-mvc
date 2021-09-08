package nextstep.mvc;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import samples.AnnotationTestController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("처리 가능 테스트")
    void supportsTest() {

        // given
        final TestController handler = new TestController();

        // when
        final boolean supports = new ControllerHandlerAdapter().supports(handler);

        // then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("처리 불가 테스트")
    void cannotSupportsTest() throws NoSuchMethodException {

        // given
        final AnnotationTestController controller = new AnnotationTestController();
        final Method method = controller.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handler = new HandlerExecution(controller, method);

        // when
        final boolean supports = new ControllerHandlerAdapter().supports(handler);

        // then
        assertThat(supports).isFalse();
    }

    @Test
    @DisplayName("핸들 테스트")
    void handleTest() throws Exception {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        TestController handler = new TestController();

        // when
        final ModelAndView modelAndView = new ControllerHandlerAdapter().handle(request, response, handler);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
