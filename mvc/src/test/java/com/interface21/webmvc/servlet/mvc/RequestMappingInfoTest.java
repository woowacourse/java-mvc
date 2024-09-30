package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

class RequestMappingInfoTest {

    @Test
    @DisplayName("부착된 어노테이션으로부터 핸들러 키 조회 성공")
    void getHandlerKeys() throws NoSuchMethodException {
        // given
        final RequestMapping requestMapping = this.getClass()
                .getDeclaredMethod("testRequestMapping").getAnnotation(RequestMapping.class);
        final RequestMappingInfo requestMappingInfo = new RequestMappingInfo(requestMapping);

        // when
        final List<HandlerKey> handlerKeys = requestMappingInfo.getHandlerKeys();

        // then
        assertAll(
                () -> assertThat(handlerKeys).hasSize(2),
                () -> assertThat(handlerKeys).containsExactly(
                        new HandlerKey("/test", RequestMethod.GET),
                        new HandlerKey("/test", RequestMethod.HEAD)
                )
        );
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.HEAD})
    private void testRequestMapping() {
    }

    @Test
    @DisplayName("부착된 어노테이션으로부터 핸들러 키 조회 성공: 메서드 정보가 없는 경우 모든 HTTP 메서드로 조회")
    void getHandlerKeys_whenNoMethod() throws NoSuchMethodException {
        // given
        final RequestMapping requestMapping = this.getClass()
                .getDeclaredMethod("testRequestMappingWithNoMethod").getAnnotation(RequestMapping.class);
        final RequestMappingInfo requestMappingInfo = new RequestMappingInfo(requestMapping);

        // when
        final List<HandlerKey> handlerKeys = requestMappingInfo.getHandlerKeys();

        // then
        assertAll(
                () -> assertThat(handlerKeys).hasSize(8),
                () -> assertThat(handlerKeys).containsExactlyInAnyOrder(
                        new HandlerKey("/test", RequestMethod.GET),
                        new HandlerKey("/test", RequestMethod.HEAD),
                        new HandlerKey("/test", RequestMethod.POST),
                        new HandlerKey("/test", RequestMethod.PUT),
                        new HandlerKey("/test", RequestMethod.PATCH),
                        new HandlerKey("/test", RequestMethod.DELETE),
                        new HandlerKey("/test", RequestMethod.OPTIONS),
                        new HandlerKey("/test", RequestMethod.TRACE)
                )
        );
    }

    @RequestMapping(value = "/test")
    private void testRequestMappingWithNoMethod() {
    }
}
