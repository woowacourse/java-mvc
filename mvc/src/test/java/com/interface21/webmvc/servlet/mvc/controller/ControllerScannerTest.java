package com.interface21.webmvc.servlet.mvc.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ControllerScannerTest {
    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp(){
        controllerScanner = ControllerScanner.from("samples");
    }

    @ParameterizedTest
    @MethodSource("creationFailureCases")
    void 컨트롤러_생성에_실패하면_예외를_던진다(String basePackage, Class<? extends Throwable> causeType) {
        ControllerScanner scanner = ControllerScanner.from(basePackage);

        assertThatThrownBy(scanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(causeType);
    }

    static Stream<Arguments> creationFailureCases() {
        return Stream.of(
                Arguments.of("creationFailureFixture.missingConstructor", NoSuchMethodException.class),
                Arguments.of("creationFailureFixture.inaccessible", IllegalAccessException.class),
                Arguments.of("creationFailureFixture.abstractType", InstantiationException.class),
                Arguments.of("creationFailureFixture.throwing", InvocationTargetException.class)
        );
    }

}
