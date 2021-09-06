package nextstep.mvc.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.mvc.exception.NotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationExceptionHandlerMappingTest {

    @DisplayName("ExceptionHandler 어노테이션을 기준으로 핸들러를 찾는다")
    @Test
    void getHandler() {
        AnnotationExceptionHandlerMapping handlerMapping = new AnnotationExceptionHandlerMapping("samples");
        Object handler = handlerMapping.getHandler(NotFoundException.class);
        ExceptionHandlerExecution handlerExecution = (ExceptionHandlerExecution) handler;

        assertThat(handlerExecution).isNotNull();
    }

    @Disabled
    @DisplayName("서로 다른 핸들러에서 같은 예외를 핸들링할 경우 에외를 발생한다. ")
    @Test
    void registerWithDuplicatedException() {
        assertThatThrownBy(() -> {
            new AnnotationExceptionHandlerMapping("samples");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
