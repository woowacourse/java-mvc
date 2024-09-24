package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ViewNameTest {

    @DisplayName("유효한 값을 생성자에 입력하면 ViewName 인스턴스를 생성해 반환한다.")
    @Test
    void createViewNameInstance() {
        // Given
        final String input = "/view/indes.jsp";

        // When
        final ViewName viewName = new ViewName(input);

        // Then
        assertThat(viewName).isNotNull();
    }

    @DisplayName("생성자에 null 혹은 공백이 입력되면 예외를 발생시킨다.")
    @NullAndEmptySource
    @ParameterizedTest
    void validateViewNameIsNullOrEmpty(final String input) {
        // When & Then
        assertThatThrownBy(() -> new ViewName(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("view name은 null 혹은 공백이 입력될 수 없습니다. - " + input);
    }

    @DisplayName("리다이렉트를 위한 url이면 true를 반환한다.")
    @Test
    void isRedirectTrue() {
        // Given
        final String input = "redirect:/view/index.jsp";
        final ViewName viewName = new ViewName(input);

        // When
        final boolean isRedirect = viewName.isRedirect();

        // Then
        assertThat(isRedirect).isTrue();
    }

    @DisplayName("리다이렉트를 위한 url이 아니면 false를 반환한다.")
    @Test
    void isRedirectFalse() {
        // Given
        final String input = "/view/index.jsp";
        final ViewName viewName = new ViewName(input);

        // When
        final boolean isRedirect = viewName.isRedirect();

        // Then
        assertThat(isRedirect).isFalse();
    }

    @DisplayName("ViewName 값에서 uri 부분만 반환한다.")
    @ValueSource(strings = {"/view/index.jsp", "redirect:/view/index.jsp"})
    @ParameterizedTest
    void getUri(final String input) {
        // Given
        final ViewName viewName = new ViewName(input);

        // When
        final String uri = viewName.getUri();

        // Then
        final String expect = "/view/index.jsp";
        assertThat(uri).isEqualTo(expect);
    }
}
