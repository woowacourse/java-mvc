package nextstep.mvc.controller.tobe.adapters;

import static nextstep.fixtures.HttpServletFixtures.createRequest;
import static nextstep.fixtures.HttpServletFixtures.createResponse;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.mappings.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    void supports() throws NoSuchMethodException {
        // given
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        TestController controller = new TestController();
        Method method = controller.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution execution = new HandlerExecution(controller, method);

        // when
        boolean isSupport = adapter.supports(execution);

        // then
        assertThat(isSupport).isTrue();
    }

    @Test
    void handle() throws Exception {
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        TestController controller = new TestController();
        Method method = controller.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution execution = new HandlerExecution(controller, method);
        // given

        // when
        ModelAndView modelAndView = adapter.handle(createRequest(), createResponse(), execution);

        // then
        assertThat(modelAndView).isNotNull();
    }
}
