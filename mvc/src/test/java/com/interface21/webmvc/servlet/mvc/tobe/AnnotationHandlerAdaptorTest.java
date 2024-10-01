package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerAdaptorTest {

    private AnnotationHandlerAdaptor handlerAdaptor;

    @BeforeEach
    void setUp() {
        handlerAdaptor = new AnnotationHandlerAdaptor();
    }

    @Test
    @DisplayName("HandlerExecution 타입의 핸들러를 지원하는 경우 true를 반환한다.")
    void isSupported_withHandlerExecution() {
        // given
        HandlerExecution handlerExecution = new HandlerExecution(null);

        // when
        boolean result = handlerAdaptor.isSupported(handlerExecution);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("HandlerExecution 타입의 핸들러를 지원하는 경우 false를 반환한다.")
    @Test
    void methodName() {
        // given
        Object otherHandler = new Object();

        // when
        boolean result = handlerAdaptor.isSupported(otherHandler);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("handle은 HandlerExecution이 요청을 처리하고 ModelAndView를 반환하는지 확인한다.")
    void handle_withHandlerExecution() {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        String expectedViewName = "viewName";

        HandlerExecution handlerExecution = new HandlerExecution(null) {
            @Override
            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
                return new ModelAndView(new JspView(expectedViewName));
            }
        };

        // when
        ModelAndView modelAndView = handlerAdaptor.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo(expectedViewName);
    }
}
