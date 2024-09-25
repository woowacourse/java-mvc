package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.RequestMappingHandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.NotFoundHandlerAdapter;
import com.interface21.webmvc.servlet.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
    }

    @DisplayName("주어진 핸들러를 처리할 핸들러 어댑터가 존재하면 핸들러를 반환한다.")
    @Test
    void givenExistHandler_thenReturnHandler() {
        Controller controller = mock(Controller.class);
        SimpleControllerHandlerAdapter controllerHandlerAdapter = new SimpleControllerHandlerAdapter();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapters.addHandlerAdapter(controllerHandlerAdapter);
        handlerAdapters.addHandlerAdapter(requestMappingHandlerAdapter);

        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(controller);
        assertThat(handlerAdapter).isInstanceOf(SimpleControllerHandlerAdapter.class);
    }

    @DisplayName("주어진 핸들러를 처리할 핸들러 어댑터가 없다면 예외를 던진다.")
    @Test
    void givenInvalidHandler_thenThrowException() {
        SimpleControllerHandlerAdapter controllerHandlerAdapter = new SimpleControllerHandlerAdapter();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapters.addHandlerAdapter(controllerHandlerAdapter);
        handlerAdapters.addHandlerAdapter(requestMappingHandlerAdapter);

        NotSupportHandler controller = mock(NotSupportHandler.class);

        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(controller))
                .isInstanceOf(NotFoundHandlerAdapter.class)
                .hasMessage("주어진 핸들러를 처리한 HandlerAdapter가 존재하지 않습니다.");

    }

    class NotSupportHandler {
    }
}
