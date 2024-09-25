package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.interface21.web.bind.annotation.RequestMethod;
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
}
