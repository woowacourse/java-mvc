package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.techcourse.controller.request.RegisterRequest;
import com.techcourse.domain.User;
import com.techcourse.exception.DuplicateAccountException;
import com.techcourse.repository.InMemoryUserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterServiceTest {

    private RegisterService registerService;
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        Map<String, User> database = new HashMap<>();
        userRepository = new InMemoryUserRepository(database, new AtomicLong(1));

        registerService = new RegisterService(userRepository);
    }

    @DisplayName("register 시도시 이미 존재하는 account가 기입되면 예외가 발생한다.")
    @Test
    void registerException() {
        // given
        String account = "account";
        userRepository.save(new User(account, "pw", "em"));

        RegisterRequest request = new RegisterRequest(account, "password", "email");

        // when, then
        assertThatThrownBy(() -> registerService.registerUser(request))
            .isExactlyInstanceOf(DuplicateAccountException.class);
    }
}