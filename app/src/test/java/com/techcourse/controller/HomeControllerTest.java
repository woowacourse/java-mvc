package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("HomeController 테스트")
class HomeControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        homeController = new HomeController();
    }

    @DisplayName("HomeController GET / 요청 테스트")
    @Test
    void getRoot() {
        // given
        // when
        final ModelAndView modelAndView = homeController.homeView(request, response);
        final View view = modelAndView.getView();

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }
}