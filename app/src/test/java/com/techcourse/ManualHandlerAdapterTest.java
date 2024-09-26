package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.LoginController;
import com.techcourse.domain.User;

import samples.TestController;

class ManualHandlerAdapterTest {

    @Test
    @DisplayName("컨트롤러 인터페이스 기반을 지원하는지 확인한다.")
    void supoort() {
        var sut = new ManualHandlerAdapter();
        var controller = new LoginController();

        assertThat(sut.support(controller)).isTrue();
    }

    @Test
    @DisplayName("컨트롤러 인터페이스 기반이 아니면 지원하지 않는다.")
    void notSupport() {
        var sut = new ManualHandlerAdapter();
        var controller = new TestController();

        assertThat(sut.support(controller)).isFalse();
    }

    @Test
    @DisplayName("request를 처리하여 ModelAndView를 반환한다.")
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(
                new User(1, "gugu", "password", "hkkang@woowahan.com")); // 필요한 경우 사용자 정보 반환

        var sut = new ManualHandlerAdapter();
        var controller = new LoginController();

        // when
        var actual = sut.handle(request, response, controller);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getView()).isInstanceOf(JspView.class)
        );
    }
}
