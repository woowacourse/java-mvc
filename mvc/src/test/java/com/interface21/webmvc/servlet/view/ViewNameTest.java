package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewNameTest {

    @Test
    @DisplayName("viewName이 null이면 생성할 수 없다")
    void nullViewName_cannotBeCreated() {
        assertThatThrownBy(() -> new ViewName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("redirect 뷰 이름은 redirect 대상 경로를 반환한다")
    void redirectViewName_returnsRedirectPath() {
        // given
        final var viewName = new ViewName("redirect:/index.jsp");

        // when
        final var redirectable = viewName.redirectable();
        final var path = viewName.path();

        // then
        assertTrue(redirectable);
        assertEquals("/index.jsp", path);
    }
}
