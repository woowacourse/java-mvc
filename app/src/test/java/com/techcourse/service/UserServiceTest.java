package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.techcourse.domain.User;
import com.techcourse.exception.UserNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @DisplayName("account으로 유저를 조회한다.")
    @Test
    void findByAccount() {
        // given
        String account = "joanne";

        // when
        final User user = userService.findByAccount(account);

        // then
        assertThat(user.getAccount()).isEqualTo(account);
    }

    @DisplayName("account으로 유저를 조회한다. - 실패, 존재하지 않는 계정")
    @Test
    void findByAccountFailed() {
        // given
        String account = "1111";

        // when - then
        assertThatThrownBy(() -> userService.findByAccount(account))
            .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("여러개 account로 유저를 조회한다.")
    @Test
    void findByAccounts() {
        // given
        String[] accounts = new String[]{"joanne", "gugu"};

        // when
        final List<User> users = userService.findByAccounts(accounts);

        // then
        assertThat(users).extracting("account")
            .contains("joanne", "gugu");
    }

    @DisplayName("여러개 account로 유저를 조회한다. - 실패, 존재하지 않는 계정")
    @Test
    void findByAccountsFailed() {
        // given
        String[] accounts = new String[]{"joanne", "2222"};

        // when - then
        assertThatThrownBy(() -> userService.findByAccounts(accounts))
            .isInstanceOf(UserNotFoundException.class);
    }
}
