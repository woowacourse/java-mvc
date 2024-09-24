package com.techcourse;

import static org.junit.jupiter.api.Assertions.*;

import com.interface21.webmvc.servlet.RequestHandler;
import com.interface21.webmvc.servlet.mvc.tobe.ManualRequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("핸들러 매핑 테스트")
class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;
    private String basePackage = "com.interface21.webmvc";

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
    }

    @DisplayName("GET 요청의 핸들러 반환")
    @Test
    void shouldCorrectlyMapControllers() throws Exception {
        // given
        handlerMapping.initialize();

        // when
        RequestHandler handler = handlerMapping.getHandler("GET", "/test");

        // then
        assertNotNull(handler);
        assertTrue(handler.getClass().isAssignableFrom(ManualRequestHandler.class));
    }

    @DisplayName("POST 요청의 핸들러 반환")
    @Test
    void shouldReturnCorrectHandler() throws Exception {
        // given
        handlerMapping.initialize();

        // when
        RequestHandler handler = handlerMapping.getHandler("POST", "/submit");

        // then
        assertNotNull(handler);
        assertEquals(ManualRequestHandler.class, handler.getClass());
    }

    @DisplayName("존재하지 않는 핸들러 요청의 핸들러 반환")
    @Test
    void shouldReturnNullForUnmappedRequest() throws Exception {
        // given
        handlerMapping.initialize();

        // when
        RequestHandler handler = handlerMapping.getHandler("GET", "/unmapped");

        // then
        assertNull(handler, "Handler should be null for unmapped requests");
    }
}
