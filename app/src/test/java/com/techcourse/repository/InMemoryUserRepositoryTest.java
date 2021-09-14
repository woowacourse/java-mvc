package com.techcourse.repository;

import com.techcourse.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    @DisplayName("InMemoryUserRepository에 User를 저장한다. - 성공")
    @Test
    void save() {
        // given
        User user = new User("joanne", "1234","joanne@woowahan.com");

        // when
        final User saveUser = InMemoryUserRepository.save(user);

        // then
        assertThat(saveUser).isNotNull();
        assertThat(saveUser.getAccount()).isEqualTo(user.getAccount());
        assertThat(saveUser.checkPassword("1234")).isTrue();
    }

    @DisplayName("account로 유저를 찾는다. - 성공")
    @Test
    void findByAccount() {
        // given
        User user = new User("joanne", "1234","joanne@woowahan.com");

        // when
        InMemoryUserRepository.save(user);

        // then
        assertThat(InMemoryUserRepository.findByAccount("joanne")).isPresent();
    }

    @DisplayName("account로 유저를 찾는다. - 실패, 존재하지 않는 account")
    @Test
    void findByAccountFailed() {
        assertThat(InMemoryUserRepository.findByAccount("melong")).isEmpty();
    }
}
