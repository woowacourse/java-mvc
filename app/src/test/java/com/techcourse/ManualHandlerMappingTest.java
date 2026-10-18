package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.LogoutController;
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
    void 첫_화면_주소에_기존_컨트롤러를_매핑한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler.getClass()).isEqualTo(ForwardController.class);
    }

    @Test
    void 로그아웃_주소에_기존_컨트롤러를_매핑한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler.getClass()).isEqualTo(LogoutController.class);
    }

    @Test
    void 컨텍스트_경로를_제외하고_기존_컨트롤러를_조회한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/mvc/logout");
        when(request.getContextPath()).thenReturn("/mvc");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler.getClass()).isEqualTo(LogoutController.class);
    }

    @Test
    void 가입_주소는_더_이상_수동_매핑하지_않는다() {
        // given
        HttpServletRequest registerRequest = mock(HttpServletRequest.class);
        HttpServletRequest oldViewRequest = mock(HttpServletRequest.class);
        when(registerRequest.getRequestURI()).thenReturn("/register");
        when(oldViewRequest.getRequestURI()).thenReturn("/register/view");
        when(registerRequest.getContextPath()).thenReturn("");
        when(oldViewRequest.getContextPath()).thenReturn("");

        // when
        Object registerHandler = handlerMapping.getHandler(registerRequest);
        Object oldViewHandler = handlerMapping.getHandler(oldViewRequest);

        // then
        assertThat(registerHandler).isNull();
        assertThat(oldViewHandler).isNull();
    }

    @Test
    void 등록되지_않은_주소에는_핸들러가_없다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getContextPath()).thenReturn("");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }
}
