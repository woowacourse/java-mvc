package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

class ManualHandlerMappingTest {

    @Test
    void 로그인뷰_매핑() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        Object handler = manualHandlerMapping.getHandler("/login/view");

        assertThat(handler).isInstanceOf(LoginViewController.class);
    }

    @Test
    void 홈뷰_매핑() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        Object handler = manualHandlerMapping.getHandler("/");

        assertThat(handler).isInstanceOf(ForwardController.class);
    }

    @Test
    void 로그인_매핑() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        Object handler = manualHandlerMapping.getHandler("/login");

        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Test
    void 로그아웃_매핑() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        Object handler = manualHandlerMapping.getHandler("/logout");

        assertThat(handler).isInstanceOf(LogoutController.class);
    }
}
