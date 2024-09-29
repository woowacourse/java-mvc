package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @DisplayName("RequestMethod[]를 인자로 받아 HandlerKey 리스트를 생성한다.")
    @Test
    void createHandlerKeyListFromRequestMethods() {
        String url = "/jazz";
        RequestMethod[] requestMethods = {RequestMethod.GET, RequestMethod.DELETE};

        List<HandlerKey> handlerKeys = HandlerKey.of(url, requestMethods);

        assertAll(
                () -> assertThat(handlerKeys.size()).isEqualTo(2),
                () -> assertThat(handlerKeys.getFirst().getRequestMethod()).isEqualTo(RequestMethod.GET),
                () -> assertThat(handlerKeys.getLast().getRequestMethod()).isEqualTo(RequestMethod.DELETE)
        );
    }
}
