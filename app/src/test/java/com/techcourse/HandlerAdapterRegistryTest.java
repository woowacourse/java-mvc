package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterController;
import com.techcourse.handleradapter.HandlerExecutionHandlerAdapter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @DisplayName("handler에 맞는 handlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapterSuccess() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(
                List.of(new HandlerExecutionHandlerAdapter()));
        RegisterController handler = new RegisterController();

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(handlerAdapter.supports(new RegisterController())).isTrue();
    }

    @DisplayName("handler에 맞는 handlerAdapter가 존재하지 않으면 에러가 발생한다.")
    @Test
    void getHandlerAdapterFail() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(
                List.of(new HandlerExecutionHandlerAdapter()));
        LoginController handler = new LoginController();

        assertThatCode(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
