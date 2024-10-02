package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerMapping mapping;

    @BeforeEach
    void setUp() {
        mapping = new AnnotationHandlerMapping("samples");
        mapping.initialize();
    }

    @DisplayName("어노테이션 기반 컨트롤러의 핸들러면 핸들링 가능하다.")
    @Test
    void canHandle() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/get-test");
        request.setMethod("GET");
        Object handler = mapping.getHandler(request);

        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        assertThat(adapter.canHandle(handler)).isTrue();
    }

    @DisplayName("핸들러를 실행한다.")
    @Test
    void handle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/get-test");
        request.setMethod("GET");
        request.setAttribute("id", 1);
        Object handler = mapping.getHandler(request);

        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        ModelAndView actual = adapter.handle(handler, request, response);
        assertThat(actual.getObject("id")).isEqualTo(1);
    }
}
