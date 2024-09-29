package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void 컨트롤러를_탐색하여_인스턴스를_등록한다() {
        // given
        String packageName = "samples";

        // when
        Map<Object, List<Method>> scanned = ControllerScanner.scan(packageName);

        // then
        assertThat(scanned).hasSize(1);
    }

    @Test
    void 컨트롤러를_탐색하여_메서드들을_등록한다() {
        // given
        String packageName = "samples";

        // when
        Map<Object, List<Method>> scanned = ControllerScanner.scan(packageName);
        List<String> methods = scanned.values().stream()
                .flatMap(List::stream)
                .map(Method::getName)
                .toList();

        // then
        assertThat(methods).containsExactlyInAnyOrder("findUserId", "save", "noneMethod");
    }
}
