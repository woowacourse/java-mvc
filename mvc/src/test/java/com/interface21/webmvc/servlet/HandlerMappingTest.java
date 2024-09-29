package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("매핑은 HTTP 요청을 처리할 핸들러 목록을 초기화한다.")
    void getHandler() throws NoSuchFieldException, IllegalAccessException {
        Field filed = handlerMapping.getClass().getDeclaredField("handlerExecutions");
        filed.setAccessible(true);

        final var handlerExecutions = (Map<HandlerKey, HandlerExecution>) filed.get(handlerMapping);

        assertThat(handlerExecutions.keySet()).hasSize(12);
    }
}
