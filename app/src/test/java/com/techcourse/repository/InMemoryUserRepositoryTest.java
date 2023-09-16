package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InMemoryUserRepositoryTest {

    @Test
    void 유저_저장_테스트() {
        User user = new User("power", "password", "power@naver.com");
        InMemoryUserRepository.save(user);

        Optional<User> power = InMemoryUserRepository.findByAccount("power");
        Assertions.assertThat(power.get().getId()).isEqualTo(2L);
    }
}
