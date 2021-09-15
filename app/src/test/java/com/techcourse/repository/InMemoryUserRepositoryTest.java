package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InMemoryUserRepository 테스트")
class InMemoryUserRepositoryTest {

    @DisplayName("저장 및 조회, 존재 확인 테스트")
    @Test
    void saveAndFindAndCheckExists() {
        // given
        final User user = new User("inbi2", "password", "inbi2@email.com");

        // when
        InMemoryUserRepository.save(user);
        final User foundUser = InMemoryUserRepository.findByAccount(user.getAccount())
                .orElseThrow(() -> new NotFoundException("해당 account의 User가 존재하지 않습니다."));

        // then
        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getAccount()).isEqualTo(user.getAccount());
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(InMemoryUserRepository.existsByAccount(user.getAccount())).isTrue();
        assertThat(InMemoryUserRepository.existsByEmail(user.getEmail())).isTrue();
    }
}