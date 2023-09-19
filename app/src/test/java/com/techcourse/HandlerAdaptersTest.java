package com.techcourse;

import com.techcourse.controller.RegisterViewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.HandlerExecutionHandlerAdapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Test
    void RegisterViewController의_적절한_HandlerAdapter_반환_확인() {
        assertThat(handlerAdapters.getHandlerAdapter(new RegisterViewController())).isInstanceOf(ControllerHandlerAdapter.class);
    }
}
