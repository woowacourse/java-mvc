package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.UnprocessableHandlerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    void 지원_가능한_핸들러일_경우_true_반환() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean actual = annotationHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지원_불가능한_핸들러일_경우_false_반환() {
        // given
        Object unknownController = new Object();
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean actual = annotationHandlerAdapter.supports(unknownController);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 핸들러를_실행하고_결과를_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        TestController controller = new TestController();
        ModelAndView modelAndView = controller.findUserId(request, response);
        when(handlerExecution.handle(request, response)).thenReturn(modelAndView);

        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        ModelAndView actual = annotationHandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void 지원_불가능한_핸들러를_실행할_경우_예외_발생() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object unknownController = new Object();

        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when, then
        assertThatThrownBy(() -> annotationHandlerAdapter.handle(request, response, unknownController))
                .isInstanceOf(UnprocessableHandlerException.class);
    }
}
