package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

import samples.TestAnnotationController;
import samples.TestExtendsController;

class HandlerAdapterRegistryTest {

    @Nested
    @DisplayName("요청 Handler에 맞는 HandlerAdapter 반환")
    class GetHandlerAdapter {

        @Test
        @DisplayName("요청 Handler에 맞는 HandlerAdapter 반환 성공: ControllerHandlerAdapter")
        void getHandlerAdapter_ControllerHandlerAdapter() {
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new TestExtendsController());

            assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
        }

        @Test
        @DisplayName("요청 Handler에 맞는 HandlerAdapter 반환 성공: HandlerExecutionHandlerAdapter")
        void getHandlerAdapter_HandlerExecutionHandlerAdapter() {
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            final HandlerExecution handlerExecution = new HandlerExecution(new TestAnnotationController(), null);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

            assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdapter.class);
        }

        @Test
        @DisplayName("요청 Handler에 맞는 HandlerAdapter 반환 실패: handler가 controller 또는 handlerExecution이 아닌 경우")
        void getHandlerAdapter_IllegalArgumentException() {
            final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
            final Object handler = new Object();

            assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 핸들러를 처리할 수 있는 어댑터가 없습니다.");
        }
    }
}
