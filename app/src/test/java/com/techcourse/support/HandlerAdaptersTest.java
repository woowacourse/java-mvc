package com.techcourse.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerAdaptersTest {

    @Test
    void 지원하는_HandlerAdapter_찾기_성공() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapters.getHandlerAdapter(handler))
            .isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @Test
    void 지원하는_HandlerAdapter가_없으면_예외() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(handler))
            .isInstanceOf(HandlerAdapterNotFoundException.class);
    }
}
