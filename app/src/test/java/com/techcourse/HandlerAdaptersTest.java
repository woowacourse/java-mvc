package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapters;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class HandlerAdaptersTest {

    private AnnotationHandlerMapping annotationHandlerMapping;
    private ManualHandlerMapping manualHandlerMapping;
    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.init();

        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.init();

        handlerAdapters = new HandlerAdapters(List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter()));
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 어댑터가 존재하지 않는 경우 예외가 발생한다.")
    void HandleAbsenceExceptionTest() {
        final var request = new MockHttpServletRequest("GET", "/absence-test");
        final var response = new MockHttpServletResponse();
        final var handler = annotationHandlerMapping.getHandler(request);
        assertThatThrownBy(() -> handlerAdapters.handle(request, response, handler))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("GET /absence-test를 처리할 수 있는 어댑터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("url, method를 어노테이션 기반 컨트롤러 어댑터로 처리할 수 있다.")
    void HandleWithAnnotationControllerTest() {
        final var request = new MockHttpServletRequest("GET", "/login/view");
        final var response = new MockHttpServletResponse();
        final var handler = annotationHandlerMapping.getHandler(request);

        assertThatCode(() -> handlerAdapters.handle(request, response, handler)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("url, method를 인터페이스 기반 컨트롤러 어댑터로 처리할 수 있다.")
    void HandleWithInterfaceControllerTest() {
        final var request = new MockHttpServletRequest("GET", "/register/view");
        final var response = new MockHttpServletResponse();
        final var handler = manualHandlerMapping.getHandler(request);

        assertThatCode(() -> handlerAdapters.handle(request, response, handler)).doesNotThrowAnyException();
    }
}
