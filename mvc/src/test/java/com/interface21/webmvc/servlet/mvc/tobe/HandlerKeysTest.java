package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerKeysTest {

    private static final String GET_METHOD_TEST_PATH = "/get-test";
    private static final String NO_METHOD_TEST_PATH = "/no-method-test";

    @Test
    @DisplayName("RequestMethod에 맞는 HandlerKey를 생성한다.")
    void should_create_request_method_when_request_method_exist() throws NoSuchMethodException {
        //given
        Method method = TestController.class
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        //when
        HandlerKey expectedHandlerKey = new HandlerKey(GET_METHOD_TEST_PATH, RequestMethod.GET);
        HandlerKeys actualHandlerKey = new HandlerKeys(requestMapping);

        //then
        assertThat(actualHandlerKey.getHandlerKeys()).contains(expectedHandlerKey);
    }

    @Test
    @DisplayName("RequestMethod가 존재하지 않으면 모든 RequestMethod에 대해 지원한다.")
    void should_support_all_request_method_when_request_method_not_exist() throws NoSuchMethodException {
        //given
        Method method = TestController.class
                .getDeclaredMethod("noMethod", HttpServletRequest.class, HttpServletResponse.class);
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        //when
        List<HandlerKey> expectedHandlerKeys
                = Arrays.stream(RequestMethod.values())
                .map(requestMethod -> new HandlerKey(NO_METHOD_TEST_PATH, requestMethod))
                .toList();
        HandlerKeys actualHandlerKeys = new HandlerKeys(requestMapping);

        //then
        assertThat(actualHandlerKeys.getHandlerKeys()).isEqualTo(expectedHandlerKeys);
    }
}
