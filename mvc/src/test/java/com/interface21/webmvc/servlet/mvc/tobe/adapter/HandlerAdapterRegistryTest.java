package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerAdapterNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 요청을_처리할_수_있는_어댑터를_반환() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        // when
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(actual).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    void 커스텀_HandlerAdapter_추가() {
        // given
        CustomHandler customHandler = new CustomHandler();
        CustomHandlerAdapter customHandlerAdapter = new CustomHandlerAdapter();
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        // when
        handlerAdapterRegistry.addHandlerAdapter(customHandlerAdapter);
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(customHandler);

        // then
        assertThat(actual).isInstanceOf(CustomHandlerAdapter.class);
    }

    @Test
    void 요청을_처리할_수_있는_어댑터가_없다면_예외_발생() {
        // given
        Object unknownController = new Object();
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        // when, then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(unknownController))
                .isInstanceOf(HandlerAdapterNotFoundException.class);
    }

    static class CustomHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(Object handler) {
            return handler instanceof CustomHandler;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return null;
        }
    }

    static class CustomHandler {
    }
}
