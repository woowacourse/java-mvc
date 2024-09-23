package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {

    @CsvSource({"/get-test, true", "/post-test, false"})
    @ParameterizedTest
    @DisplayName("파라미터로 주어진 HandlerKey의 url과 비교 대상 HandlerKey의 url을 비교하고 동일 url 여부를 반환한다.")
    void isSameUrl(String requestURI, String expected) {
        //given
        HandlerKey handlerKey = new HandlerKey("/get-test", RequestMethod.GET);
        HandlerKey parameter = new HandlerKey(requestURI, RequestMethod.GET);

        //when
        boolean isSameUrl = handlerKey.isSameUrl(parameter);

        //then
        assertThat(isSameUrl).isEqualTo(Boolean.valueOf(expected));
    }
}
