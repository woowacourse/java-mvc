package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdaptor;
import nextstep.mvc.view.ModelAndView;
import samples.TestController;

public class AnnotationHandlerAdaptorTest {

    @Test
    @DisplayName("적용 가능한 handler인지 여부를 반환한다.")
    void supportHandler(){

        // given
        HandlerAdaptor handlerAdaptor = new AnnotationHandlerAdaptor();
        TestController testController = new TestController();
        Method method = testController.getClass().getDeclaredMethods()[0];
        HandlerExecution handlerExecution = new HandlerExecution(testController, method);

        // when, then
        assertThat(handlerAdaptor.supports(handlerExecution)).isTrue();
    }

    @Test
    @DisplayName("handler 실행 후 ModelAndView를 반환한다.")
    void handle() throws Exception {

        // given
        HandlerAdaptor handlerAdaptor = new AnnotationHandlerAdaptor();
        TestController testController = new TestController();
        Method method = testController.getClass().getDeclaredMethods()[0];
        HandlerExecution handlerExecution = new HandlerExecution(testController, method);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute(any())).thenReturn("panda");

        // when
        ModelAndView modelAndView = handlerAdaptor.handle(request, response, handlerExecution);
        String id = String.valueOf(modelAndView.getObject("id"));

        // when
        assertThat(id).isEqualTo("panda");
    }
}
