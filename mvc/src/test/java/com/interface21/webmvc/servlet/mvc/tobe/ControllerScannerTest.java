package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("컨트롤러 애노테이션이 달려 있다면 스캔 후 인스턴스를 저장한다.")
    @Test
    void getControllers() {
        ControllerScanner scanner = new ControllerScanner("samples");

        List<Object> objects = scanner.getControllers().values().stream().toList();

        assertThat(objects.getFirst())
                .isInstanceOf(TestController.class);
    }
}
