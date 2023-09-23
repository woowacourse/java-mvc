package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.ManualHandlerAdapter;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdaptersTest {

    @Mock
    Controller controllerHandler;

    @Mock
    HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void controller_handler를_지원하는_adapter를_반환한다() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(controllerHandler);

        // then
        assertThat(ManualHandlerAdapter.class.isAssignableFrom(handlerAdapter.getClass())).isTrue();
    }

    @Test
    void handler_execution를_지원하는_adapter를_반환한다() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handlerExecution);

        // then
        assertThat(AnnotationHandlerAdapter.class.isAssignableFrom(handlerAdapter.getClass())).isTrue();
    }

    @Test
    void 지원하는_adapter가_없을_경우_예외를_던진다() {
        // given
        Object wrongHandler = "wrongHandler";
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // expect
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(wrongHandler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하는 Adapter가 존재하지 않습니다.");
    }

    @Test
    void adapter를_추가한다() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // expect
        assertThatNoException().isThrownBy(() -> handlerAdapters.add(new AnnotationHandlerAdapter()));
    }
}
