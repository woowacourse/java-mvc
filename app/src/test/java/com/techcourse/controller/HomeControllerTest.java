package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    @DisplayName("/ 경로로 GET 요청시 index.jsp 를 반환한다.")
    @Test
    void home() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.home(request, response);

        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("index.jsp")));
    }
}
