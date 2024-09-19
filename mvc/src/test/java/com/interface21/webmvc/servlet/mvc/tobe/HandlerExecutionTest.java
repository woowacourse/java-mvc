package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import samples.TestController;

class HandlerExecutionTest {

    @Test
    @DisplayName("httpServletRequest과 httpServletResponse를 매개변수로 받고 반환값이 HandlerKey이면 method.invoke한다.")
    void invoke_method_when_method_params_is_http_servlet_request_and_http_servlet_response_with_return_type_handler_key()
            throws NoSuchMethodException {
        // given
        final TestController testController = new TestController();
        final Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(testController, method);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when & then
        assertThatCode(() -> handlerExecution.handle(request, response))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("HttpServletRequest와 HttpServletResponse가 아니라면 예외를 던진다.")
    void throw_exception_when_insert_invalid_method_parameters() throws NoSuchMethodException {
        // given
        final ArrayList<Object> objects = new ArrayList<>();
        final Method method = objects.getClass()
                .getDeclaredMethod("size");
        final HandlerExecution handlerExecution = new HandlerExecution(objects, method);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when & then
        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
