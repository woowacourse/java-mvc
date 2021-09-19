package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.techcourse.controller.request.RegisterRequest;
import com.techcourse.domain.User;
import com.techcourse.exception.DuplicateAccountException;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserService는")
class UserServiceTest {

    private UserService userService;
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        Map<String, User> database = new ConcurrentHashMap<>();
        userRepository = new InMemoryUserRepository(database, new AtomicLong(1));

        userService = new UserService(userRepository);
    }

    @DisplayName("account로 유저 탐색시 일치하는 유저가 없으면 예외가 발생한다.")
    @Test
    void findUserByAccountException() {
        // when, then
        assertThatThrownBy(() -> userService.findUserByAccount("라이언"))
            .isExactlyInstanceOf(UserNotFoundException.class);
    }
}