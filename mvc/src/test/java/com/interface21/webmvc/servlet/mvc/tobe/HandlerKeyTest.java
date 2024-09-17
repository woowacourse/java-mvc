package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @DisplayName("url과 method 메핑에 성공한다.")
    @Test
    void successCreate() throws NoSuchMethodException {
        // given
        Class<?> clazz = TestClass.class;
        Method method = clazz.getDeclaredMethod("testMethod");
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        RequestMethod requestMethod = annotation.method()[0];
        HandlerKey expected = new HandlerKey("/test/post-url", RequestMethod.GET);

        // when
        HandlerKey actual = new HandlerKey(annotation.value(), requestMethod);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("uri 형태가 잘못되면 예외를 발생시킨다")
    @Test
    void failCreateWhenInvalidUrlFormat() throws NoSuchMethodException {
        // given
        Class<?> clazz = TestClass.class;
        Method method = clazz.getDeclaredMethod("invalidUrlFormat");
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        RequestMethod requestMethod = annotation.method()[0];

        // when
        assertThatThrownBy(() -> new HandlerKey(annotation.value(), requestMethod))
                .isInstanceOf(IllegalArgumentException.class);

    }

    private static class TestClass {

        @RequestMapping(value = "/test/post-url", method = {RequestMethod.GET})
        void testMethod() {

        }

        @RequestMapping(value = "invalidUrlFormat", method = {RequestMethod.GET, RequestMethod.POST})
        void invalidUrlFormat() {

        }
    }
}