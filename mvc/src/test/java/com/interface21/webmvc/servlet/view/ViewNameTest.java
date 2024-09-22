package com.interface21.webmvc.servlet.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ViewNameTest {

    @DisplayName("리다이렉트가 아닌 일반 URL을 처리할 때")
    @Test
    void handleNormalViewName() {
        //given & when
        String page = "/test.html";
        ViewName viewName = new ViewName(page);

        //then
        assertThat(viewName.getViewName()).isEqualTo(page);
    }

    @DisplayName("리다이렉트 URL을 처리할 때 'redirect:' 접두사를 제거")
    @Test
    void handleRedirectViewName() {
        //given & when
        String redirect = "redirect:";
        String page = "/test.html";
        ViewName viewName = new ViewName(redirect + page);

        //then
        assertThat(viewName.getViewName()).isEqualTo(page);
    }
}
