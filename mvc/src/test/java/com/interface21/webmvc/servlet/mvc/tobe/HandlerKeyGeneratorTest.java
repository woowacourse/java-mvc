package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyGeneratorTest {

    @DisplayName("Method에 RequestMapping 어노테이션이 붙지 않으면 예외가 발생한다.")
    @Test
    void exceptionWhenRequestMappingNotExist() throws NoSuchMethodException {
        // given
        Method method = TestClass.class.getDeclaredMethod("exceptionOccurred");

        // when
        assertThatThrownBy(() -> HandlerKeyGenerator.fromAnnotatedMethod(method))
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("RequestMapping에 기입되어 있는 RequestMethod로 HandlerKey 생성한다.")
    @Test
    void generateGetHttpRequestMethod() throws NoSuchMethodException {
        // given
        Method method = TestClass.class.getDeclaredMethod("generateGetHttpRequestMethod");
        HandlerKey expected = new HandlerKey("/generate-get-http-request-method", RequestMethod.GET);

        // when
        List<HandlerKey> handlerKeys = HandlerKeyGenerator.fromAnnotatedMethod(method);
        HandlerKey actual = handlerKeys.stream()
                .filter(handlerKey -> handlerKey.equals(expected))
                .findFirst()
                .get();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("RequestMethod를 지정하지 않으면, 모든 RequestMethod에 대한 HandlerKey 생성한다.")
    @Test
    void generatedAllHttpRequestMethod() throws NoSuchMethodException {
        // given
        Method method = TestClass.class.getDeclaredMethod("generateAllHttpRequestMethod");
        int expected = RequestMethod.values().length;

        // when
        List<HandlerKey> handlerKeys = HandlerKeyGenerator.fromAnnotatedMethod(method);
        int actual = handlerKeys.size();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    class TestClass {

        @RequestMapping("/generate-all-http-request-method")
        void generateAllHttpRequestMethod() {
        }

        @RequestMapping(value = "/generate-get-http-request-method", method = RequestMethod.GET)
        void generateGetHttpRequestMethod() {
        }

        void exceptionOccurred() {
        }
    }
}