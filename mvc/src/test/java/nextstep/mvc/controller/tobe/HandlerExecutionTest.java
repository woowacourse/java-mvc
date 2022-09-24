package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
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

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        HandlerExecution handlerExecution = new HandlerExecution(testController, method);

        // when
        final ModelAndView result = handlerExecution.handle(request, response);

        // then
        assertAll(() -> {
            assertThat(result.getView()).isInstanceOf(JspView.class);
            assertThat(result.getModel()).containsEntry("id", "gugu");
        });
    }
}