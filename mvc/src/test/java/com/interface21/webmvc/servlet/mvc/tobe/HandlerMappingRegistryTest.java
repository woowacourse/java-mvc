package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMappingRegistryTest {

    private final HandlerMapping testHandlerMapping = new HandlerMapping() {

        @Override
        public Object getHandler(HttpServletRequest request) {
            return "test";
        }
    };

    @Test
    @DisplayName("핸들러 반환 성공 : 핸들러가 존재하는 경우")
    void getHandler_exist() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(testHandlerMapping);

        // when
        Optional<Object> handler = registry.getHandler(new MockHttpServletRequest());

        //then
        assertThat(handler).isPresent()
                .get()
                .isEqualTo("test");
    }

    @Test
    @DisplayName("핸들러 반환 성공 : 핸들러가 존재하지 않는 경우")
    void getHandler_notExist() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();

        // when
        Optional<Object> handler = registry.getHandler(new MockHttpServletRequest());

        // then
        assertThat(handler).isEmpty();
    }
}
