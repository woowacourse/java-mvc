package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RequestMappingInfoTest {

    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    @DisplayName("지정한 HTTP 메서드에 대한 핸들러 키를 생성한다")
    void createHandlerKeys_withSpecifiedRequestMethods(RequestMethod requestMethod) {
        //given
        final String testURI = "/test";

        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(
                testURI,
                new RequestMethod[]{requestMethod}
        );

        //when
        List<HandlerKey> handlerKeys = requestMappingInfo.handlerKeys();

        //then
        assertEquals(1, handlerKeys.size());
        assertEquals(new HandlerKey(testURI, requestMethod), handlerKeys.getFirst());
    }

    @Test
    @DisplayName("HTTP 메서드가 지정되지 않으면 모든 HTTP 메서드에 대한 핸들러 키를 생성한다")
    void createHandlerKeys_withoutSpecifiedRequestMethods() {
        // given
        final String testURI = "/test";
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(
                testURI,
                new RequestMethod[]{}
        );

        //when
        List<HandlerKey> handlerKeys = requestMappingInfo.handlerKeys();

        //then
        assertEquals(RequestMethod.values().length, handlerKeys.size());
    }
}
