package nextstep.mvc.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.handler.exception.ExceptionHandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ExceptionHandlerExecutionTest {

    @DisplayName("ExceptionHandler 를 호출한다.")
    @Test
    void handle() throws Exception {
        ExceptionHandlerExecution handlerExecution = ExceptionHandlerExecution.of(TestController.class);
        ModelAndView mv = handlerExecution.handle(new UnHandledRequestException());

        assertThat(mv.getViewName()).isEqualTo(ViewName.of("404.html"));
    }

    @DisplayName("예외 처리된 Exception으로 ExceptionHandler 를 호출한다.")
    @Test
    void catchException() throws Exception {
        ExceptionHandlerExecution handlerExecution = ExceptionHandlerExecution.of(TestController.class);
        try {
            throw new UnHandledRequestException();
        } catch (Exception e){
            ModelAndView mv = handlerExecution.handle(e);
            assertThat(mv.getViewName()).isEqualTo(ViewName.of("404.html"));
        }
    }
}
