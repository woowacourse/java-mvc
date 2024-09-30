package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.exception.HandlerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerManagerTest {

    @DisplayName("서로 다른 핸들러 매핑에 같은 요청 매핑이 등록되어 있을 경우 먼저 등록한 핸들러 매핑이 응답한다.")
    @Test
    void handlerMappingOrder() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        FirstHandlerMapping firstHandler = new FirstHandlerMapping();
        SecondHandlerMapping secondHandler = new SecondHandlerMapping();
        HandlerManager manager = new HandlerManager(firstHandler, secondHandler);

        when(request.getRequestURI()).thenReturn("/same-url");
        when(request.getMethod()).thenReturn("GET");

        // when
        manager.handle(request, response);
        String firstResult = firstHandler.getResult();
        String secondResult = secondHandler.getResult();

        // then
        assertAll(
                () -> assertThat(firstResult).isEqualTo("first handler mapping processed"),
                () -> assertThat(secondResult).isEmpty()
        );
    }

    @DisplayName("요청을 처리할 수 있는 핸들러가 존재 하지 않을 경우 예외가 발생한다.")
    @Test
    void handlerNotFoundException() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        FirstHandlerMapping firstHandler = new FirstHandlerMapping();
        HandlerManager manager = new HandlerManager(firstHandler);

        when(request.getRequestURI()).thenReturn("/handler-not-found");
        when(request.getMethod()).thenReturn("GET");

        // when
        assertThatThrownBy(() -> manager.handle(request, response))
                // then
                .isInstanceOf(HandlerNotFoundException.class);
    }


    class FirstHandlerMapping implements ServletRequestHandler {
        private String result = "";

        @Override
        public boolean canHandle(HttpServletRequest request) {
            return request.getRequestURI().equals("/same-url");
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
            result = "first handler mapping processed";
        }

        @Override
        public void initialize() {
        }

        public String getResult() {
            return result;
        }
    }

    class SecondHandlerMapping implements ServletRequestHandler {
        private String result = "";

        @Override
        public boolean canHandle(HttpServletRequest request) {
            return request.getRequestURI().equals("/same-url");
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
            result = "second handler mapping processed";
        }

        @Override
        public void initialize() {
        }

        public String getResult() {
            return result;
        }
    }
}
