package com.interface21.webmvc.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.techcourse.controller.LoginController;

class HandlerAdapterContainerTest {

    @Test
    @DisplayName("핸들러 어댑터 찾기 : AnnotationHandlerAdapter")
    void findHandlerAdapterWithAnnotationHandlerAdapter() {
        HandlerAdapterContainer container = new HandlerAdapterContainer("com");
        LoginController instance = new LoginController();

        HandlerAdapter handlerAdapter = container.findHandlerAdapter(
                new HandlerExecution(instance, instance.getClass().getEnclosingMethod()));

        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("핸들러 어댑터 찾기 : 해당 핸들러를 지원하는 HandlerAdapter가 없음")
    void findHandlerAdapterFail() {
        HandlerAdapterContainer container = new HandlerAdapterContainer("com");
        Object handler = "notExistsHandler";

        assertThatThrownBy(() -> container.findHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 핸들러를 지원하는 HandlerAdapter를 찾을 수 없습니다: " + handler);
    }
}
