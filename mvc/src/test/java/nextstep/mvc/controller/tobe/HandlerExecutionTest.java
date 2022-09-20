package nextstep.mvc.controller.tobe;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    @DisplayName("handle 메소드는 생성자 매개변수로 입력 받은 컨트롤러의 메소드를 실행하고 실행 결과를 ModelAndView 객체로 반환한다.")
    void handle() throws Exception {
        // given
        final TestController testController = new TestController();
        final Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(testController, method);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        final ModelAndView result = handlerExecution.handle(request, response);

        // then
        Assertions.assertThat(result.getView()).isInstanceOf(JspView.class);
    }
}