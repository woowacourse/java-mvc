package nextstep.mvc.controller.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.RequestFixture;
import fixture.ResponseFixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import samples.TestAnnotationController;

class HandlerExecutionTest {

    @DisplayName("객체에 정의된 메서드 실행")
    @Test
    void handle() throws Exception {
        final Object object = TestAnnotationController.class.getConstructor().newInstance();
        final Method method = object.getClass().getMethods()[0];
        final HandlerExecution handlerExecution = new HandlerExecution(object, method);

        final HttpServletRequest request = RequestFixture.getRequest();
        final HttpServletResponse response = ResponseFixture.response();

        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
