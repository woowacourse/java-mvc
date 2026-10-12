package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("모델과 뷰")
class ModelAndViewTest {

    @Test
    @DisplayName("모델 이름이 null이면 등록 시점에 거부한다")
    void rejectsNullAttributeName() {
        // given
        final var modelAndView = new ModelAndView(mock(View.class));

        // when & then
        assertThatThrownBy(() -> modelAndView.addObject(null, "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("모델 이름은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("모델 값이 null이면 그대로 등록할 수 있다")
    void acceptsNullAttributeValue() {
        // given
        final var modelAndView = new ModelAndView(mock(View.class));

        // when
        modelAndView.addObject("user", null);

        // then
        assertThat(modelAndView.getModel()).containsEntry("user", null);
    }

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
