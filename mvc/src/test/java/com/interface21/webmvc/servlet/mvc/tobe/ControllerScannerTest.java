package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestAnnotationController;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller 어노테이션이 붙은 클래스를 찾아 반환 성공")
    void getControllers() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");
        Set<Class<?>> controllers = controllerScanner.getControllers();
        assertThat(controllers).containsOnly(TestAnnotationController.class);
    }

    @Test
    @DisplayName("Controller 어노테이션이 붙은 클래스를 찾아 반환 성공: 없는 경우 빈 Set 반환")
    void getControllersWhenNotExist() {
        final ControllerScanner controllerScanner = new ControllerScanner("hi");
        Set<Class<?>> controllers = controllerScanner.getControllers();
        assertThat(controllers).isEmpty();
    }
}
