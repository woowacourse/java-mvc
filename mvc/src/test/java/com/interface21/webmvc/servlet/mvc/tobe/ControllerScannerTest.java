package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("@Controller가 붙은 클래스를 찾아 인스턴스를 생성한다")
    void scan() {
        // when
        Map<Class<?>, Object> controllers = new ControllerScanner().scan("samples");

        // then
        assertEquals(Set.of(TestController.class), controllers.keySet());
        assertInstanceOf(TestController.class, controllers.get(TestController.class));
    }
}
