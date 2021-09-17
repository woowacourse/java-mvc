package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicatedUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryUserRepositoryTest {

    @BeforeEach
    void setUp() {
        InMemoryUserRepository.removeAll();
    }

    @DisplayName("인메모리 DB에 유저를 저장한다.")
    @Test
    void save() {
        // given
        User newUser = new User(1L, "charlie", "1234", "test@test.com");

        // when
        InMemoryUserRepository.save(newUser);

        // then
        User findUser = InMemoryUserRepository.findByAccount(newUser.getAccount()).get();
        assertThat(findUser).isEqualTo(newUser);
    }

    @DisplayName("인메모리 DB에 중복된 계정의 유저를 저장한다.")
    @Test
    void saveDuplicatedAccount() {
        // given
        User newUser1 = new User(1L, "charlie", "1234", "test@test.com");
        User newUser2 = new User(2L, "charlie", "1234", "test@test.com");
        InMemoryUserRepository.save(newUser1);

        // when then
        assertThatThrownBy(() -> InMemoryUserRepository.save(newUser2))
                .isInstanceOf(DuplicatedUserException.class);
    }

    @DisplayName("저장된 유저의 정보를 조회할 수 있다.")
    @Test
    void findByAccount() {
        // given
        String account = "charlie";
        User newUser = new User(1L, account, "1234", "test@test.com");
        InMemoryUserRepository.save(newUser);

        // when
        User findUser = InMemoryUserRepository.findByAccount(account).get();

        // then
        assertThat(findUser.getAccount()).isEqualTo(account);
    }

    @DisplayName("존재하지 않는 유저를 조회하려고 한다.")
    @Test
    void findByAccountWithNonExistUser() {
        // given
        String nonExistAccount = "dsfkmasklfmsklfe";

        // when
        Optional<User> byAccount = InMemoryUserRepository.findByAccount(nonExistAccount);

        // then
        assertThat(byAccount).isEmpty();
    }
}