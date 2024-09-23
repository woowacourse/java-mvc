package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.AnnotationController;

class HandlerExecutionsTest {

    private final AnnotationController controller = new AnnotationController();

    @DisplayName("컨트롤러에 존재하는 handler 를 저장한다.")
    @Test
    void setHandlerExecutions() {
        HandlerExecutions handlerExecutions = new HandlerExecutions();

        handlerExecutions.setHandlerExecutions(controller);

        assertThatCode(() -> handlerExecutions.findByUrlAndMethod("/get-test", RequestMethod.GET))
                .doesNotThrowAnyException();
    }

    @DisplayName("경로와 HttpMethod 에 맞는 핸들러가 존재하지 않으면 예외가 발생한다.")
    @Test
    void findByUrlAndMethod() {
        HandlerExecutions handlerExecutions = new HandlerExecutions();

        assertThatThrownBy(() -> handlerExecutions.findByUrlAndMethod("/post-test", RequestMethod.POST))
                .isInstanceOf(NoSuchElementException.class);
    }
}
