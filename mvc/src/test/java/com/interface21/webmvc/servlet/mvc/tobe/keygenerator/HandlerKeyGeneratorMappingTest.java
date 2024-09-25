package com.interface21.webmvc.servlet.mvc.tobe.keygenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyGeneratorMappingTest {

    @Test
    @DisplayName("Handler Key를 만들수 있는 메이커가 없는 경우 null을 반환한다.")
    void match_WhenNoAnnotationMethod() throws NoSuchMethodException {
        Class<?> clazz = TestController.class;
        Method method = clazz.getDeclaredMethod("noAnnotationMethod");

        assertThat(new HandlerKeyGeneratorMapping().match(method))
                .isNull();
    }
}
