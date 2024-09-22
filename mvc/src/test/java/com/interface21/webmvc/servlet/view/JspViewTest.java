package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("redirect를 사용한 viewName: 참")
    void isRedirectView() {
        assertThat(JspView.isRedirectView("redirect:/")).isTrue();
    }

    @Test
    @DisplayName("redirect를 사용한 viewName: 거짓")
    void isRedirectView_WhenNotRedirect() {
        assertThat(JspView.isRedirectView("/")).isFalse();
    }

    @Test
    @DisplayName("redirect 키워드를 제외한 viewName 반환 성공")
    void getRedirectUrl() {
        assertThat(JspView.getRedirectUrl("redirect:/")).isEqualTo("/");
    }

    @Test
    @DisplayName("redirect 키워드를 제외한 viewName 반환 성공")
    void getRedirectUrl_WhenNotRedirect() {
        assertThatThrownBy(() -> JspView.getRedirectUrl("/"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("redirect를 사용하지 않은 viewName입니다.");
    }
}
