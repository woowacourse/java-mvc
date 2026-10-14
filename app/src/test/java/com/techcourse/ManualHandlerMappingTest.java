package com.techcourse;

import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    void 가입_화면_주소에_기존_컨트롤러를_매핑한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register/view");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler.getClass()).isEqualTo(RegisterViewController.class);
    }

    @Test
    void 가입_처리_주소에_기존_컨트롤러를_매핑한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler.getClass()).isEqualTo(RegisterController.class);
    }

    @Test
    void 등록되지_않은_주소에는_핸들러가_없다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }
}
