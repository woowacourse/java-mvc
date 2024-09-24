package com.interface21.webmvc.servlet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestSimpleController;

class HandlerAdapterTest {

    private Map<String, Controller> controllers;
    private List<HandlerAdapter> adapters;
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setAdapters() {
        controllers = new HashMap<>();
        controllers.put("/test", new TestSimpleController());

        adapters = new ArrayList<>();
        adapters.add(new SimpleControllerHandlerAdapter());
        adapters.add(new AnnotationHandlerAdapter());

        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("핸들러 어댑터는 Legacy MVC와 @MVC 모두 지원할 수 있어야 한다.")
    void supports() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final var simpleController = controllers.get("/test");
        final var annotatedController = handlerMapping.getHandler(request);

        // then
        assertAll(
                () -> assertTrue(adapters.getFirst().supports(simpleController)),
                () -> assertTrue(adapters.getLast().supports(annotatedController))
        );
    }
}
