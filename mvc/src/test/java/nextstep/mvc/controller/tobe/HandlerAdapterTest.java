package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class HandlerAdapterTest {

    @DisplayName("컨트롤러 핸들러를 실행시킬 수 있다.")
    @Test
    void executeController() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = new TestController();

        when(request.getAttribute("name")).thenReturn("tonic");

        // when
        ModelAndView actual = HandlerAdapter.handle(request, response, controller);

        // then
        Field field = JspView.class
                .getDeclaredField("viewName");
        field.setAccessible(true);
        String viewName = (String) field.get(actual.getView());
        assertThat(viewName).isEqualTo("tonic");
    }

    @DisplayName("HandlerExecution 핸들러를 실행시킬 수 있다.")
    @Test
    void handleHandlerExecution() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Method findUserIdMethod = TestController.class
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), findUserIdMethod);
        when(request.getAttribute("id")).thenReturn("tonic");

        // when
        ModelAndView actual = HandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(actual.getObject("id")).isEqualTo("tonic");
    }
}
