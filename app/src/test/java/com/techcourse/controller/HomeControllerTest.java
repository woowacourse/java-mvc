package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomeControllerTest extends ControllerTest {

    private HomeController homeController;

    @DisplayName("/ 요청 시 index.jsp의 ModelAndView를 리턴한다.")
    @Test
    void show() {
        // given
        homeController = new HomeController();
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");

        assertThatCode(() -> homeController.show(request, response))
            .doesNotThrowAnyException();

        final ModelAndView modelAndView = homeController.show(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("/index.jsp");
    }
}
