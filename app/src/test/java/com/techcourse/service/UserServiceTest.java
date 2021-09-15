package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserService 비즈니스 로직 테스트")
class UserServiceTest {

    private static final String EXISTING_ACCOUNT = "gugu";

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @DisplayName("account로 회원 조회하기 - 성공")
    @Test
    void findByAccount() {
        // given
        // when
        final User foundAccount = userService.findByAccount(EXISTING_ACCOUNT);

        // then
        assertThat(foundAccount.getAccount()).isEqualTo(EXISTING_ACCOUNT);
    }

    @DisplayName("account로 회원 조회하기 - 실패 - 존재하지 않는 account")
    @Test
    void findByAccountFailureWhenNotExists() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userService.findByAccount(EXISTING_ACCOUNT + 2))
                .isInstanceOf(NotFoundException.class);
    }
}