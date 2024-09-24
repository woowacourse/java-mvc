package com.interface21.webmvc.servlet.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;
import samples.TestController;

@DisplayName("컨트롤러 어댑터")
class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter controllerHandlerAdapter;

    @BeforeEach
    void setUp() {
        this.controllerHandlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("Controller 인터페이스를 구현한 핸들러의 지원이 가능하다.")
    @Test
    void supportsValidController() {
        // given
        Controller controller = new TestController();

        // when
        boolean actual = controllerHandlerAdapter.supports(controller);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("Controller 인터페이스를 구현하지 않은 핸들러는 지원하지 않는다.")
    @Test
    void supportsInvalidController() {
        // given
        TestAnnotationController testAnnotationController = new TestAnnotationController();

        // when
        boolean actual = controllerHandlerAdapter.supports(testAnnotationController);

        // then
        assertThat(actual).isFalse();
    }
}
