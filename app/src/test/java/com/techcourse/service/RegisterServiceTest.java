package com.techcourse.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.domain.User;
import com.techcourse.service.dto.RegisterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterServiceTest {

    private static final RegisterService registerService = new RegisterService();

    @DisplayName("회원가입을 한다. - 성공")
    @Test
    void join() {
        // given
        String account = "새로운_계정";
        String password = "새로운_비밀번호";
        String email = "joanne@woowahan.com";

        // when
        final User user = registerService.join(RegisterDto.of(account, password, email));

        // then
        assertThat(user).isNotNull();
        assertThat(user.getAccount()).isEqualTo(account);
    }
}
