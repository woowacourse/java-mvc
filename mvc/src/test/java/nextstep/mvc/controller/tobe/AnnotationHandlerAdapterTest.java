package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import nextstep.web.support.RequestUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @Test
    void handler_execution_객체인지_확인한다() throws NoSuchMethodException {
        // given
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        Method method = Object.class.getMethod("equals", Object.class);

        // when
        Object handlerExecution = new HandlerExecution(new Object(), method);
        Object notHandlerExecution = new RequestUrl("/eden");

        // then
        Assertions.assertAll(
                () -> assertThat(annotationHandlerAdapter.supports(handlerExecution)).isTrue(),
                () -> assertThat(annotationHandlerAdapter.supports(notHandlerExecution)).isFalse()
        );
    }
}
