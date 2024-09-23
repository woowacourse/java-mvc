package com.interface21.webmvc.servlet.mvc.tobe.keygenerator;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RequestMappingHandlerKeyGeneratorTest {

    @Test
    @DisplayName("RequestMethod가 없는 경우 모든 RequestMethod를 매핑하여 저장한다.")
    void makeKeys_WhenNoRequestMethod() throws NoSuchMethodException {
        RequestMappingHandlerKeyGenerator requestMappingKeyMaker = new RequestMappingHandlerKeyGenerator();
        Class<?> clazz = TestController.class;
        Method method = clazz.getDeclaredMethod("findUserName", HttpServletRequest.class, HttpServletResponse.class);

        assertAll(
                () -> assertThat(requestMappingKeyMaker.hasAnnotation(method)).isTrue(),
                () -> assertThat(requestMappingKeyMaker.makeKeys(method)).hasSize(RequestMethod.values().length)
        );
    }
}
