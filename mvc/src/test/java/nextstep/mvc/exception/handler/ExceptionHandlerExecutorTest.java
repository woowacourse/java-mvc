package nextstep.mvc.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExceptionHandlerExecutorTest {

    @DisplayName("ExceptionHandler 어노테이션을 기준으로 핸들러를 찾는다")
    @Test
    void getHandler() throws Exception {
        ExceptionHandlerExecutor handlerExecutor = new ExceptionHandlerExecutor("samples");
        ModelAndView modelAndView = handlerExecutor.execute(new UnHandledRequestException());

        assertThat(modelAndView.getViewName()).isEqualTo("404.html");
    }

    @Disabled
    @DisplayName("서로 다른 핸들러에서 같은 예외를 핸들링할 경우 에외를 발생한다. ")
    @Test
    void registerWithDuplicatedException() {
        assertThatThrownBy(() -> {
            ExceptionHandlerExecutor handlerExecutor = new ExceptionHandlerExecutor("samples");
            new ExceptionHandlerExecutor("samples");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
