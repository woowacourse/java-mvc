package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("모델과 뷰")
class ModelAndViewTest {

    @Test
    @DisplayName("View가 없으면 생성 시점에 거부한다")
    void rejectsNullView() {
        // given
        final View view = null;

        // when & then
        assertThatThrownBy(() -> new ModelAndView(view))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("View는 null일 수 없습니다.");
    }
}
