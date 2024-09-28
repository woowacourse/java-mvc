package com.interface21.webmvc.servlet.mvc.handler.adapter;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerAdaptersTest {

    private AnnotationHandlerMapping handlerMapping;
    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.success");
        handlerMapping.init();
        handlerAdapters = new HandlerAdapters(List.of(new AnnotationHandlerAdapter()));
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 어댑터가 존재하지 않는 경우 예외가 발생한다.")
    void HandleAbsenceExceptionTest() {
        final var request = new MockHttpServletRequest("GET", "/absence-test");
        final var response = new MockHttpServletResponse();
        final var handler = handlerMapping.getHandler(request);
        assertThatThrownBy(() -> handlerAdapters.handle(request, response, handler))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("GET /absence-test를 처리할 수 있는 어댑터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 핸들러를 찾을 수 있다.")
    void HandleTest() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/get-test");
        final var response = new MockHttpServletResponse();
        final var handler = handlerMapping.getHandler(request);

        assertThatCode(() -> handlerAdapters.handle(request, response, handler)).doesNotThrowAnyException();
    }
}
