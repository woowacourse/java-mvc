package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterViewController;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @Test
    void 로그인_support_true() {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        boolean result = manualHandlerAdapter.supports(new LoginController());

        assertThat(result).isTrue();
    }

    @Test
    void register페이지_support_false() {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        boolean result = manualHandlerAdapter.supports(new RegisterViewController());

        assertThat(result).isFalse();
    }
}
