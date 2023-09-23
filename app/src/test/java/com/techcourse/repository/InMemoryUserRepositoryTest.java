package com.techcourse.repository;

import com.techcourse.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InMemoryUserRepositoryTest {

    @Test
    void 인메모리에_사용자를_저장한_후_계정으로_조회할_수_있다() {
        // given
        final User user = new User(4L, "hyena", "hyena", "hyena@test.com");

        // when
        InMemoryUserRepository.save(user);
        final Optional<User> maybeHyena = InMemoryUserRepository.findByAccount("hyena");

        // then
        assertThat(maybeHyena).contains(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"gugu", "hyena", "herb"})
    void 인메모리에_기본으로_3명의_사용자가_자동으로_저장되어_있다(final String account) {
        // given
        final Optional<User> maybeUser = InMemoryUserRepository.findByAccount(account);

        // expect
        assertThat(maybeUser).isPresent();
    }
}
