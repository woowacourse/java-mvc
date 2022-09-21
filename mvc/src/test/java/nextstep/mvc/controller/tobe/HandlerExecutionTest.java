package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;

class HandlerExecutionTest {

    @Test
    @DisplayName("HandlerExecution이 findUserId 메서드를 올바르게 실행시키는지 확인한다.")
    void handle() throws Exception {
        // given
        Reflections reflections = new Reflections("samples");
        Class<?> controller = reflections.getTypesAnnotatedWith(Controller.class).iterator().next();
        Method method = controller.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        ModelAndView actual = handlerExecution.handle(request, response);
        String actualId = (String)actual.getObject("id");

        // then
        assertThat(actualId).isEqualTo("gugu");
    }

}
