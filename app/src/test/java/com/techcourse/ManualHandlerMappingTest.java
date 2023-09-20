package com.techcourse;

import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private ManualHandlerMapping manualHandlerMapping;

    @BeforeEach
    void setup() {
        manualHandlerMapping = new ManualHandlerMapping();
    }

    @DisplayName("컨트롤러를 uri로 찾아온다")
    @Test
    void find_controller_by_uri() {
        // given
        HttpServletRequest req = mock(HttpServletRequest.class);
        manualHandlerMapping.initialize();

        when(req.getRequestURI()).thenReturn("/login");

        // when
        Controller result = manualHandlerMapping.getHandler(req);

        // then
        assertThat(result).isInstanceOf(LoginController.class);
    }
}
