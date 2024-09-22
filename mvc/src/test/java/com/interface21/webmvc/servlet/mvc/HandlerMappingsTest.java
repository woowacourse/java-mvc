package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    static class DummyHandlerMapping implements HandlerMapping {

        public DummyHandlerMapping(String test) {}

        public void initialize() {

        }

        public Object getHandler(HttpServletRequest request) {
            return null;
        }
    }

    @DisplayName("기본 생성자가 없는 HandlerMapping이 있을 경우 예외를 발생시킨다")
    @Test
    void notExistDefaultConstructor() {
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.initialize();
    }

    @DisplayName("")
    @Test
    void notExistMatchHandler() {
        HandlerMappings handlerMappings = new HandlerMappings();
    }
}
