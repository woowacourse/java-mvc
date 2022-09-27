package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    void handle()
            throws Exception {
        Class<TestController> testControllerClass = TestController.class;
        Object testControllerInstance = testControllerClass.getDeclaredConstructor().newInstance();
        Method requestMappingMethod = Arrays.stream(testControllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .findAny()
                .get();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("someThing");
        HandlerExecution handlerExecution = new HandlerExecution(testControllerInstance, requestMappingMethod);

        Object returnObject = handlerExecution.handle(request, response);

        assertThat(returnObject).isInstanceOf(ModelAndView.class);
    }

}
