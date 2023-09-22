package com.techcourse.support.mvc.adapter;


import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.controller.LoginController;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;

public class HandlerAdaptersTest {

    @Test
    @DisplayName("핸들러에 맞는 HandlerAdapter를 반환한다.")
    void getHandlerAdapter() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());

        LoginController loginController = new LoginController();

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(loginController);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
