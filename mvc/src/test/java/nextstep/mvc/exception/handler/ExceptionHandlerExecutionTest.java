package nextstep.mvc.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.mvc.exception.NotFoundException;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ExceptionHandlerExecutionTest {

    @DisplayName("ExceptionHandler 를 호출한다.")
    @Test
    void handle() throws Exception {
        AnnotationExceptionHandlerMapping handlerMapping = new AnnotationExceptionHandlerMapping("samples");
        Object handler = handlerMapping.getHandler(NotFoundException.class);

        ExceptionHandlerExecution handlerExecution = (ExceptionHandlerExecution) handler;
        ModelAndView mv = handlerExecution.handle(new NotFoundException());

        assertThat(mv.getViewName()).isEqualTo("404.html");
    }

    @DisplayName("예외 처리된 Exception으로 ExceptionHandler 를 호출한다.")
    @Test
    void catchException() throws Exception {
        AnnotationExceptionHandlerMapping handlerMapping = new AnnotationExceptionHandlerMapping("samples");
        Object handler = handlerMapping.getHandler(NotFoundException.class);

        ExceptionHandlerExecution handlerExecution = (ExceptionHandlerExecution) handler;

        try {
            throw new NotFoundException();
        } catch (Exception e){
            ModelAndView mv = handlerExecution.handle(e);
            assertThat(mv.getViewName()).isEqualTo("404.html");
        }
    }
}