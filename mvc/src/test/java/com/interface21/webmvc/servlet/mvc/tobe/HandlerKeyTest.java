package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.web.bind.annotation.RequestMethod;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HandlerKeyTest {

    @DisplayName("동일한 URL과 RequestMethod로 생성된 인스턴스는 동일해야한다.")
    @Test
    void getHandlerKey() {
        // given
        String url = "/test";
        RequestMethod method = RequestMethod.GET;

        // when
        HandlerKey firstInstance = HandlerKey.of(url, method);
        HandlerKey secondInstance = HandlerKey.of(url, method);

        // then
        assertThat(firstInstance).isSameAs(secondInstance);
    }

    @DisplayName("다른 URL이거나 RequestMethod이면 새로운 인스턴스를 생성한다.")
    @Test
    void createHandlerKey() {
        // given
        String url = "/test";
        RequestMethod method = RequestMethod.GET;

        // when
        HandlerKey firstInstance = HandlerKey.of(url, method);
        HandlerKey secondInstance = HandlerKey.of(url, RequestMethod.POST);

        // then
        assertThat(firstInstance).isNotSameAs(secondInstance);
    }
}
