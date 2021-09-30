package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HelloControllerTest extends ControllerTest{

    @DisplayName("hello 요청 시 json으로 응답한다.")
    @Test
    void hello() {
        // given
        HelloController helloController = new HelloController();

        // when
        when(request.getRequestURI()).thenReturn("/api/hello");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("Hello")).thenReturn("Hello, Hi");

        final ModelAndView modelAndView = helloController.hello(request, response);

        // then
        assertThat(modelAndView.view()).isInstanceOf(JsonView.class);
        assertThat(modelAndView.model()).containsKey("Hello");
    }
}
