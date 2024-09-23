package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import samples.AnnotationController;
import samples.SimpleController;

class SimpleHandlerAdapterTest {

    @DisplayName("인터페이스 구현체 기반 컨트롤러면 핸들링 가능하다.")
    @Test
    void canHandle1() {
        SimpleHandlerAdapter simpleHandlerAdapter = new SimpleHandlerAdapter();
        SimpleController simpleController = new SimpleController();

        assertThat(simpleHandlerAdapter.canHandle(simpleController)).isTrue();
    }

    @DisplayName("인터페이스 구현체 기반 컨트롤러가 아니면 핸들링 불가능하다.")
    @Test
    void canHandle2() {
        SimpleHandlerAdapter simpleHandlerAdapter = new SimpleHandlerAdapter();
        AnnotationController annotationController = new AnnotationController();

        assertThat(simpleHandlerAdapter.canHandle(annotationController)).isFalse();
    }

    @DisplayName("핸들러를 실행한다.")
    @Test
    void handle() throws Exception {
        SimpleHandlerAdapter simpleHandlerAdapter = new SimpleHandlerAdapter();
        SimpleController simpleController = new SimpleController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("test", 1);
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView actual = simpleHandlerAdapter.handle(simpleController, request, response);

        assertThat(actual.getObject("test")).isEqualTo(1);
    }
}
