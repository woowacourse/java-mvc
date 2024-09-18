package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("핸들러 키 테스트")
class HandlerKeyTest {

    @DisplayName("HandlerKey 생성자를 통한 인스턴스 생성 테스트")
    @Test
    void create() {
        // given
        HandlerKey handlerKey = new HandlerKey("/test-url", RequestMethod.GET);

        // when&then
        assertThat(handlerKey).isNotNull();
        assertThat(handlerKey.toString()).contains("/test-url", "GET");
    }

    @DisplayName("String으로 생성된 HandlerKey와 RequestMethod로 생성된 HandlerKey가 동일한지 테스트")
    @Test
    void createWithStringMethod() {
        // given
        HandlerKey handlerKey1 = new HandlerKey("/test-url", RequestMethod.GET);
        HandlerKey handlerKey2 = new HandlerKey("/test-url", "GET");

        // when&then
        assertThat(handlerKey1).isEqualTo(handlerKey2);
    }
}
