package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RequestMethodTest {

    @ParameterizedTest
    @DisplayName("문자열로 메스드 인스턴스를 가져온다.")
    @EnumSource(RequestMethod.class)
    void get_instance_of_request_method_via_string_name(final RequestMethod method) {
        // given
        final String name = method.name();

        // when
        final RequestMethod actual = RequestMethod.from(name);

        // then
        assertThat(actual).isSameAs(method);
    }

    @Test
    @DisplayName("정의되지 않은 메스드 입력시 예외를 발생시킨다.")
    void throw_exception_when_insert_not_define_method_string_name() {
        // given
        final String invalidMethod = "MIN";

        // when & then
        assertThatThrownBy(() -> RequestMethod.from(invalidMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("문자열로 인스턴스 조회는 case-sensitive하다.")
    void search_instance_via_string_is_case_sensitive() {
        // given
        final String lowerCaseMethodString = "get";

        // when & then
        assertThatThrownBy(() -> RequestMethod.from(lowerCaseMethodString))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
