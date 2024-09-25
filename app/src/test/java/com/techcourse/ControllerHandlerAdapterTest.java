package com.techcourse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import samples.AnnotationTestController;
import samples.TestController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("ManualHandler를 지원한다.")
    @Test
    void supports() {
        // given
        Controller controller = new TestController();

        // when
        boolean actual = handlerAdapter.supports(controller);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("ManualHandler외의 handler는 지원하지 않는다.")
    @Test
    void cannotSupport() {
        // given
        AnnotationTestController controller = new AnnotationTestController();

        // when
        boolean actual = handlerAdapter.supports(controller);

        // then
        assertThat(actual).isFalse();
    }
}
