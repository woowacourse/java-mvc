package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.interface21.web.bind.annotation.RequestMethod;

import samples.TestController;

class HandlerKeyCreatorTest {

    @Test
    @DisplayName("RequestMapping 애노테이션의 값으로 키를 생성한다.")
    void create_handler_key_via_request_mapping_annotation_internal_values() throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);
        final HandlerKeyCreator handlerKeyCreator = new HandlerKeyCreator(method);

        // when
        final List<HandlerKey> handlerKeys = handlerKeyCreator.create();

        // then
        assertThat(handlerKeys).containsExactly(new HandlerKey("/get-test", RequestMethod.GET));
    }

    @ParameterizedTest
    @DisplayName("request_method가 빈값이면 모든 method로 키를 만든다.")
    @EnumSource(value = RequestMethod.class)
    void create_all_handler_key_when_request_method_is_empty(final RequestMethod requestMethod)
            throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod("all", HttpServletRequest.class,
                HttpServletResponse.class);
        final HandlerKeyCreator handlerKeyCreator = new HandlerKeyCreator(method);

        // when
        final List<HandlerKey> handlerKeys = handlerKeyCreator.create();

        // then
        assertThat(handlerKeys)
                .contains(new HandlerKey("/all-test", requestMethod));
    }
}
