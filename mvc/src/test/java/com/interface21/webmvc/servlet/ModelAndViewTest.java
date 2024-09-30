package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ModelAndViewTest {

    @DisplayName("뷰 객체를 통해 ModelAndView를 생성한 경우 뷰 객체를 반환한다.")
    @Test
    void getView() {
        ModelAndView modelAndView = new ModelAndView(new JspView("index.jsp"));

        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JspView.class),
                () -> assertThat(modelAndView.getViewName()).isNull()
        );
    }

    @DisplayName("뷰 이름을 통해 ModelAndView를 생성한 경우 뷰 이름을 반환한다.")
    @Test
    void getViewName() {
        ModelAndView modelAndView = new ModelAndView("index.jsp");

        assertAll(
                () -> assertThat(modelAndView.getViewName()).isInstanceOf(String.class),
                () -> assertThat(modelAndView.getView()).isNull()
        );
    }
}
