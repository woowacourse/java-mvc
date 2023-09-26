package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HandlerExecutionTest {

    @Test
    @DisplayName("handle 메서드의 시그니처와 동일하면 생성할 수 있다")
    void construct() {
        //given
        final TestController object = new TestController();
        final Method method = object.getClass().getMethods()[0];

        //when, then
        assertDoesNotThrow(() -> new HandlerExecution(object, method));
    }

    @Test
    @DisplayName("handle 메서드의 시그니처와 다르면 예외가 발생한다 - 첫 번째 인자")
    void construct_fail1() throws NoSuchMethodException {
        //given
        final IllegalController object = new IllegalController();
        final Method method = object.getClass().getMethod("noRequest", int.class, HttpServletResponse.class);

        //when, then
        assertThatThrownBy(() -> new HandlerExecution(object, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("HttpServletRequest가 필요합니다.");
    }

    @Test
    @DisplayName("handle 메서드의 시그니처와 다르면 예외가 발생한다 - 두 번째 인자")
    void construct_fail2() throws NoSuchMethodException {
        //given
        final IllegalController object = new IllegalController();
        final Method method = object.getClass().getMethod("noResponse", HttpServletRequest.class, int.class);

        //when, then
        assertThatThrownBy(() -> new HandlerExecution(object, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("HttpServletResponse가 필요합니다.");
    }

    @Test
    @DisplayName("handle 메서드의 시그니처와 다르면 예외가 발생한다 - 리턴 타입")
    void construct_fail3() throws NoSuchMethodException {
        //given
        final IllegalController object = new IllegalController();
        final Method method = object.getClass().getMethod("noView", HttpServletRequest.class, HttpServletResponse.class);

        //when, then
        assertThatThrownBy(() -> new HandlerExecution(object, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ModelAndView를 리턴해야 합니다.");
    }

    @Test
    @DisplayName("handle 메서드의 시그니처와 다르면 예외가 발생한다 - 인자 갯수")
    void construct_fail4() throws NoSuchMethodException {
        //given
        final IllegalController object = new IllegalController();
        final Method method = object.getClass().getMethod("invalidArgumentsSize", HttpServletRequest.class,
                HttpServletResponse.class, int.class);

        //when, then
        assertThatThrownBy(() -> new HandlerExecution(object, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("인자는 2개여야 합니다.");
    }

    private static class IllegalController {

        public ModelAndView noRequest(final int illegal, final HttpServletResponse response) {
            return null;
        }

        public ModelAndView noResponse(final HttpServletRequest request, final int illegal) {
            return null;
        }

        public Integer noView(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }

        public ModelAndView invalidArgumentsSize(final HttpServletRequest request, final HttpServletResponse response,
                                                 final int illegal) {
            return null;
        }
    }
}
