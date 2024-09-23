package com.interface21.webmvc.servlet.mvc.tobe.keymaker;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RequestMappingKeyMakerTest {

    @Test
    @DisplayName("RequestMethod가 없는 경우 모든 RequestMethod를 매핑하여 저장한다.")
    void makeKeys_WhenNoRequestMethod() throws NoSuchMethodException {
        RequestMappingKeyMaker requestMappingKeyMaker = new RequestMappingKeyMaker();
        Class<?> clazz = TestController.class;
        Method method = clazz.getDeclaredMethod("findUserName", HttpServletRequest.class, HttpServletResponse.class);

        assertAll(
                () -> assertThat(requestMappingKeyMaker.hasAnnotation(method)).isTrue(),
                () -> assertThat(requestMappingKeyMaker.makeKeys(method).length)
                        .isEqualTo(RequestMethod.values().length)
        );
    }
}
