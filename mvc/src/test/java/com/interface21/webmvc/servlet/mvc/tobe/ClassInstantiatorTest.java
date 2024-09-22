package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.context.stereotype.Controller;

import samples.TestController;

class ClassInstantiatorTest {

    @Test
    @DisplayName("클래스로부터 인스턴스 생성 성공")
    void Instantiate() {
        final Object instance = ClassInstantiator.Instantiate(TestController.class);

        assertAll(
                () -> assertNotNull(instance),
                () -> assertInstanceOf(TestController.class, instance)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidClasses")
    @DisplayName("클래스로부터 인스턴스 생성 실패: 인스턴스화가 불가능한 타입")
    void instantiate_When(Class<?> clazz) {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> ClassInstantiator.Instantiate(clazz));
    }

    static Stream<Class<?>> invalidClasses() {
        return Stream.of(
                Runnable.class,
                AbstractClass.class,
                SampleEnum.class,
                Controller.class,
                int.class,
                void.class,
                String[].class
        );
    }

    static abstract class AbstractClass {
    }

    enum SampleEnum {
        VALUE1, VALUE2
    }
}
