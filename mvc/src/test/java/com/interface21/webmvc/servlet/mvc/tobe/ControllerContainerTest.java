package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerContainerTest {

    private ControllerContainer container;

    @BeforeEach
    void setUp() {
        container = new ControllerContainer("samples");
        container.initialize();
    }

    @Test
    @DisplayName("Controller 어노테이션이 붙은 객체들을 생성한다.")
    void container_controller_initialize() {
        final Object testController = container.getController(TestController.class);
        final Object sampleController = container.getController(SampleController.class);
        assertThat(testController).isInstanceOf(TestController.class);
        assertThat(sampleController).isInstanceOf(SampleController.class);
    }

    @Test
    @DisplayName("없는 인스턴스 찾을 시, 예외를 발생합니다.")
    void throw_exception_when_not_exist_instance() {
        assertThatThrownBy(() -> container.getController(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> container.getController(String.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
