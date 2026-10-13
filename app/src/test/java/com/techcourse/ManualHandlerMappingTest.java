package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMapping = manualHandlerMapping;
    }

    @Test
    void 요청_URL에_등록된_기존_컨트롤러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(ForwardController.class);
    }

    @Test
    void 어노테이션으로_전환한_로그인_주소는_수동_매핑에서_제외한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 어노테이션으로_전환한_로그인_화면_주소는_수동_매핑에서_제외한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 어노테이션으로_전환한_로그아웃_주소는_수동_매핑에서_제외한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/logout");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
