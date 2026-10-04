package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JspView;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelAndViewTest {

    @Test
    void View가_null이면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new ModelAndView(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할_때_전달한_View를_가진다() {
        final View view = new JspView("/index.jsp");

        final ModelAndView modelAndView = new ModelAndView(view);

        assertThat(modelAndView.getView()).isSameAs(view);
    }
}
