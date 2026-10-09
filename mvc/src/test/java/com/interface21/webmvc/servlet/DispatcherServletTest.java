package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DispatcherServletTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("basePackage가 없으면 생성할 수 없다")
    void rejectsMissingBasePackage(final String basePackage) {
        assertThatThrownBy(() -> new DispatcherServlet(basePackage))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
