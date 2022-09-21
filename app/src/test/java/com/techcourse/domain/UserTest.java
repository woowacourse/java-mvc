package com.techcourse.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode;

class UserTest {

    @DisplayName("checkPassword 메서드는")
    @Nested
    class CheckPassword {

        @DisplayName("비밀번호가 일치할 때 true를 반환한다")
        @Nested
        class ValidInput {

            @ParameterizedTest(name = "실제:{0} 입력:{1} 결과:{2}")
            @CsvSource(value = {"password,Password,false", "password,password,true"})
            void checkPassword_return_true_if_input_is_correct(String preset, String input, boolean expected) {
                // given
                final User user = new User("account", preset, "email");

                // when
                final boolean actual = user.checkPassword(input);

                // then
                assertThat(actual).isEqualTo(expected);
            }
        }

        @DisplayName("null 또는 빈 문자열 입력 시 false를 반환한다")
        @Nested
        class InvalidInput {

            @ParameterizedTest(name = "실제:password 입력:{0}")
            @NullAndEmptySource
            @DisplayName("checkPassword 메서드는 비밀번호가 일치할 때 true를 반환한다")
            void checkPassword_return_false_if_input_is_null_or_empty(String input) {
                // given
                final User user = new User("account", "password", "email");

                // when
                final boolean actual = user.checkPassword(input);

                // then
                assertThat(actual).isFalse();
            }
        }
    }

    @DisplayName("User 생성 시")
    @Nested
    class Create {

        @DisplayName("password가 유효하지 않으면 예외가 발생한다")
        @Nested
        class InvalidPassword {

            @ParameterizedTest(name = "password 입력 : {0}")
            @NullAndEmptySource
            void user_cannot_be_created_with_null_or_empty_password(String nullOrEmptyPassword) {
                assertThatThrownBy(() -> new User("account", nullOrEmptyPassword, "email"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("password를 확인해주세요");
            }
        }
    }

    @DisplayName("assignId 메서드는")
    @Nested
    class AssignId {

        @DisplayName("매개변수로 전달한 ID로 새롭게 생성한 객체를 반환한다")
        @Test
        void assignId_should_create_another_instance() {
            // given
            final User user = new User(1L, "account", "password", "email");
            final long expected = 2L;

            // when
            final User idAssigned = user.assignId(expected);

            // TOP_DOWN 으로 Field를 조회할 경우, 알파벳 오름차순으로 Field 명으로 index를 지닌 List<Object>를 반환
            // 따라서, account, id, email, password 순으로 List<Object>에 할당되어 반환됨.
            final List<Object> userFieldValues = ReflectionUtils.readFieldValues(
                    ReflectionUtils.findFields(User.class, field -> true, HierarchyTraversalMode.TOP_DOWN), user);
            final List<Object> idAssignedFieldValues = ReflectionUtils.readFieldValues(
                    ReflectionUtils.findFields(User.class, field -> true, HierarchyTraversalMode.TOP_DOWN), idAssigned);

            // then
            // assignId 메서드의 수행 결과, 다른 객체가 반환되었고, id 필드값만 할당한 값으로 변경되었고, 나머지 필드는 기존과 동일함을 검증
            assertAll(
                    () -> assertThat(userFieldValues).isNotEqualTo(idAssignedFieldValues),
                    () -> assertThat(userFieldValues.get(0)).isEqualTo(idAssignedFieldValues.get(0)),
                    () -> assertThat(userFieldValues.get(1)).isNotEqualTo(idAssignedFieldValues.get(1)),
                    () -> assertThat(idAssignedFieldValues.get(1)).isEqualTo(expected),
                    () -> assertThat(userFieldValues.get(2)).isEqualTo(idAssignedFieldValues.get(2)),
                    () -> assertThat(userFieldValues.get(3)).isEqualTo(idAssignedFieldValues.get(3))
            );
        }
    }
}
