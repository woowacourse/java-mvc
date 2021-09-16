package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        TestController testController = new TestController();
        Method findUserId = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        handlerExecution = new HandlerExecution(findUserId, testController);
    }

    @Test
    void handle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView).isNotNull();
    }
}