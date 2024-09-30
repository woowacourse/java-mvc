package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.controller.Controller;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappings;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class HandlerMappingsTest {

    private AnnotationHandlerMapping annotationHandlerMapping;
    private ManualHandlerMapping manualHandlerMapping;
    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.init();

        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.init();

        handlerMappings = new HandlerMappings(List.of(annotationHandlerMapping, manualHandlerMapping));
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 핸들러가 존재하지 않는 경우 예외가 발생한다.")
    void getHandlerAbsenceExceptionTest() {
        final var request = new MockHttpServletRequest("GET", "/absence-test");
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("GET /absence-test를 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("url, method를 처리하는 어노테이션 기반 컨트롤러를 찾을 수 있다.")
    void getHandlerWithAnnotationControllerTest() {
        final var request = new MockHttpServletRequest("GET", "/login/view");
        final var handler = handlerMappings.getHandler(request);
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("url, method를 처리하는 인터페이스 기반 컨트롤러를 찾을 수 있다.")
    void getHandlerWithInterfaceControllerTest() {
        final var request = new MockHttpServletRequest("GET", "/register/view");
        final var handler = handlerMappings.getHandler(request);
        assertThat(handler).isInstanceOf(Controller.class);
    }
}
