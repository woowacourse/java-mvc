package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class HandlerExecutionStorageTest {

    private static final String TEST_URI = "/test";

    private HandlerExecutionStorage storage;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        storage = new HandlerExecutionStorage();
        handlerExecution = new HandlerExecution(new Object(), Object.class.getMethod("toString"));
    }

    @Test
    @DisplayName("지정한 HTTP 메서드로만 핸들러를 조회할 수 있다")
    void get_withSpecifiedRequestMethod() {
        // given
        storage.add(handlerExecution, List.of(new HandlerKey(TEST_URI, RequestMethod.GET)));

        // when
        HandlerExecution foundByGet = storage.get(new HandlerKey(TEST_URI, RequestMethod.GET));
        HandlerExecution foundByPost = storage.get(new HandlerKey(TEST_URI, RequestMethod.POST));

        // then
        assertSame(handlerExecution, foundByGet);
        assertNull(foundByPost);
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    @DisplayName("HTTP 메서드가 지정되지 않으면 모든 HTTP 메서드로 핸들러를 조회할 수 있다")
    void get_withoutSpecifiedRequestMethods(RequestMethod requestMethod) {
        // given
        storage.add(handlerExecution, Arrays.stream(RequestMethod.values())
                .map(method -> new HandlerKey(TEST_URI, method))
                .toList());

        // when
        HandlerExecution found = storage.get(new HandlerKey(TEST_URI, requestMethod));

        // then
        assertSame(handlerExecution, found);
    }

    @Test
    @DisplayName("등록되지 않은 URL이면 null을 반환한다")
    void get_withUnregisteredUrl() {
        // given
        storage.add(handlerExecution, List.of(new HandlerKey(TEST_URI, RequestMethod.GET)));

        // when
        HandlerExecution found = storage.get(new HandlerKey("/unknown", RequestMethod.GET));

        // then
        assertNull(found);
    }
}
