package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

class ForwardControllerTest {

    @DisplayName("기본 index 페이지를 반환한다.")
    @Test
    void show() {
        // given
        final ForwardController forwardController = new ForwardController();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView expected = new ModelAndView(new JspView("/index.jsp"));

        // when
        final ModelAndView actual = forwardController.show(request, response);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
