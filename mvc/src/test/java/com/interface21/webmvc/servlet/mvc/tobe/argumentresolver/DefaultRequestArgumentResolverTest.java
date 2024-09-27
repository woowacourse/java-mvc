package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DefaultRequestArgumentResolverTest {

    @DisplayName("HttpServletRequest를 지원한다.")
    @Test
    void supportsTrue() {
        DefaultRequestArgumentResolver resolver = new DefaultRequestArgumentResolver();
        Parameter parameter = TestClass.class.getMethods()[0].getParameters()[0];
        boolean supports = resolver.supports(parameter);

        assertThat(supports).isTrue();
    }

    @DisplayName("HttpServletRequest가 아니면 지원하지 않는다.")

    @Test
    void supportsFalse() {
        DefaultRequestArgumentResolver resolver = new DefaultRequestArgumentResolver();
        Parameter parameter = TestClass.class.getMethods()[0].getParameters()[1];
        boolean supports = resolver.supports(parameter);

        assertThat(supports).isFalse();
    }

    @DisplayName("HttpServletResponse를 반환한다.")
    @Test
    void resolveArgument() {
        DefaultRequestArgumentResolver resolver = new DefaultRequestArgumentResolver();
        Parameter parameter = TestClass.class.getMethods()[0].getParameters()[0];
        Object argument = resolver.resolveArgument(new MockHttpServletRequest(), new MockHttpServletResponse(), parameter);

        assertThat(argument)
                .isInstanceOf(HttpServletRequest.class);
    }

    private static class TestClass {

        public String test(HttpServletRequest request, HttpServletResponse response) {
            return "";
        }
    }
}
