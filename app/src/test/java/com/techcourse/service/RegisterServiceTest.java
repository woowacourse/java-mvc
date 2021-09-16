package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicateException;
import com.techcourse.exception.NotFoundException;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RegisterService 비즈니스 로직 테스트")
class RegisterServiceTest {

    private static final String EXISTING_ACCOUNT = "gugu";
    private static final String EXISTING_EMAIL = "hkkang@woowahan.com";
    private static final String NEW_ACCOUNT = "inbi3";
    private static final String NEW_ACCOUNT_PASSWORD = "password";
    private static final String NEW_EMAIL = "inbi3@email.com";

    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        registerService = new RegisterService();
    }

    @DisplayName("회원가입 - 성공")
    @Test
    void register() {
        // given
        // when
        final User registeredUser = registerService.register(NEW_ACCOUNT, NEW_ACCOUNT_PASSWORD, NEW_EMAIL);
        final User savedUserInDB = InMemoryUserRepository.findByAccount(NEW_ACCOUNT)
                .orElseThrow(() -> new NotFoundException("해당 account의 User가 존재하지 않습니다."));

        // then
        assertThat(registeredUser.getId())
                .isNotNull()
                .isEqualTo(savedUserInDB.getId());

        assertThat(registeredUser.getAccount())
                .isEqualTo(savedUserInDB.getAccount())
                .isEqualTo(NEW_ACCOUNT);

        assertThat(registeredUser.getPassword())
                .isEqualTo(savedUserInDB.getPassword())
                .isEqualTo(NEW_ACCOUNT_PASSWORD);

        assertThat(registeredUser.getEmail())
                .isEqualTo(savedUserInDB.getEmail())
                .isEqualTo(NEW_EMAIL);
    }

    @DisplayName("회원가입 실패 - 이미 존재하는 account")
    @Test
    void registerFailureWhenAccountAlreadyExists() {
        // given
        // when
        // then
        assertThatThrownBy(() -> registerService.register(EXISTING_ACCOUNT, "password", NEW_EMAIL + 1))
                .isInstanceOf(DuplicateException.class);

        assertThat(InMemoryUserRepository.existsByEmail(NEW_EMAIL + 1)).isFalse();
    }

    @DisplayName("회원가입 실패 - 이미 존재하는 email")
    @Test
    void registerFailureWhenEmailAlreadyExists() {
        // given
        // when
        // then
        assertThatThrownBy(() -> registerService.register(EXISTING_ACCOUNT + 1, "password", EXISTING_EMAIL))
                .isInstanceOf(DuplicateException.class);

        assertThat(InMemoryUserRepository.existsByAccount(EXISTING_ACCOUNT + 1)).isFalse();
    }
}