package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;

import samples.TestController;

class HandlerMappingsTest {
    @Test
    @DisplayName("핸들러 매핑에 핸들러를 추가한다")
    void addAndGetHandlerTest() throws NoSuchMethodException {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();
        Method method = TestController.class.getDeclaredMethod("any", HttpServletRequest.class, HttpServletResponse.class);

        // when & then
        assertThatNoException().isThrownBy(() -> handlerMappings.addHandler(method, "/all-test", RequestMethod.GET));
    }

    @Test
    @DisplayName("중복된 핸들러를 추가하려고 하면 예외를 던진다")
    void duplicateHandlerKeyTest() throws NoSuchMethodException {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();
        Method method = TestController.class.getDeclaredMethod("any", HttpServletRequest.class, HttpServletResponse.class);
        handlerMappings.addHandler(method, "/all-test", RequestMethod.GET);
        HandlerKey handlerKey = new HandlerKey("/all-test", RequestMethod.GET);

        // when & then
        assertThatThrownBy(() -> handlerMappings.addHandler(method, "/all-test", RequestMethod.GET))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HandlerExecution mappings already exists with handler key: " + handlerKey);
    }
}
