package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.techcourse.controller.LoginController;
import com.techcourse.support.AnnotatedController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @DisplayName("Controller를 support하고 @Controller는 support하지 않는다")
    @Test
    void supports() {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        assertAll(
                () -> assertThat(manualHandlerAdapter.supports(new LoginController())).isTrue(),
                () -> assertThat(manualHandlerAdapter.supports(new AnnotatedController())).isFalse()
        );
    }
}
