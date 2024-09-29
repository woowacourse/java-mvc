package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller 등록 성공")
    void constructControllerScanner() {
        //given
        ControllerScanner controllerScanner = new ControllerScanner(new Object[] {"samples"});

        //when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        //then
        assertThat(controllers.values()).anyMatch(controller -> controller instanceof TestController);
    }
}
