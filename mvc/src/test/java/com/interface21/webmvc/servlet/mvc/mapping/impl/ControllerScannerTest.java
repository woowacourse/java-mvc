package com.interface21.webmvc.servlet.mvc.mapping.impl;

import com.interface21.context.stereotype.Controller;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @Test
    @DisplayName("특정 패키지에 있는 컨트롤러 어노테이션이 붙은 컨트롤러를 스캔한다")
    void scan() {
        // given
        Reflections reflections = new Reflections();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        System.out.println("스캔된 클래스들 = " + classes.size());
    }
}
