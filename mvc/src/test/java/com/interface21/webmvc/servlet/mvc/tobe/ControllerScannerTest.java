package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;
import samples.TestController2;

class ControllerScannerTest {

    @Test
    @DisplayName("Scan every annotated controllers.")
    void scan() {
        // given
        var sut = new ControllerScanner("samples");

        // when
        var controllers = sut.scan();

        // then
        assertThat(controllers.keySet()).containsOnly(TestController.class, TestController2.class);
    }
}
