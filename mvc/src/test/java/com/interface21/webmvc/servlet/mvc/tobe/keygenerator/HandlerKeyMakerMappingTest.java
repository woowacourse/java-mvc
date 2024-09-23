package com.interface21.webmvc.servlet.mvc.tobe.keygenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerKeyMakerMappingTest {

    @Test
    @DisplayName("Handler Key를 만들수 있는 메이커가 없는 경우 에러를 발생한다.")
    void match_WhenNoAnnotationMethod() throws NoSuchMethodException {
        Class<?> clazz = TestController.class;
        Method method = clazz.getDeclaredMethod("noAnnotationMethod");

        assertThatThrownBy(() -> new HandlerKeyMakerMapping().match(method))
                .isInstanceOf(IllegalStateException.class);
    }
}
