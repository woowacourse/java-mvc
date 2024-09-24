package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import samples.SampleController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionTest {

    final HttpServletRequest request = new MockHttpServletRequest();
    final HttpServletResponse response = new MockHttpServletResponse();

    @Test
    @DisplayName("Controller 내 Method 를 실행한다.")
    void execute_method_in_controller() throws Exception {
        final SampleController controller = new SampleController();
        final Method method = controller.getClass()
                .getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);

        final HandlerExecution execution = new HandlerExecution(controller, method);

        final var view1 = (ModelAndView) execution.handle(request, response);
        final var view2 = controller.test(request, response);

        assertThat(view1.getModel("test")).isEqualTo("test");
        assertThat(view2.getModel("test")).isEqualTo("test");
    }
}
