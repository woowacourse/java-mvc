package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultResponseArgumentResolverTest {

    @DisplayName("HttpServletResponse를 지원한다.")
    @Test
    void supportsTrue() {
        DefaultResponseArgumentResolver resolver = new DefaultResponseArgumentResolver();
        Parameter parameter = TestClass.class.getMethods()[0].getParameters()[1];
        boolean supports = resolver.supports(parameter);

        assertThat(supports).isTrue();
    }

    @DisplayName("HttpServletResponse가 아니면 지원하지 않는다.")
    @Test
    void supportsFalse() {
        DefaultResponseArgumentResolver resolver = new DefaultResponseArgumentResolver();
        Parameter parameter = TestClass.class.getMethods()[0].getParameters()[0];
        boolean supports = resolver.supports(parameter);

        assertThat(supports).isFalse();
    }

    private static class TestClass {

        public String test(HttpServletRequest request, HttpServletResponse response) {
            return "";
        }
    }

}
