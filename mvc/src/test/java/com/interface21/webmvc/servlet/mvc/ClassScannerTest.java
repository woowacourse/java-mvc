package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.web.bind.annotation.RequestMapping;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ClassScannerTest {

    private ClassScanner classScanner;

    @BeforeEach
    void setUp() {
        classScanner = new ClassScanner(new Object[]{"samples"});
    }

    @DisplayName("클래스 스캐너가 @RequestMapping 메서드를 스캔하는지 테스트")
    @Test
    void findHandlingMethods() {
        // when
        List<Method> handlingMethods = classScanner.findHandlingMethods();

        // then
        assertAll(
                () -> assertThat(handlingMethods).hasSize(3),
                () -> handlingMethods.forEach(method ->
                        assertThat(method.isAnnotationPresent(RequestMapping.class)).isTrue())
        );
    }
}
