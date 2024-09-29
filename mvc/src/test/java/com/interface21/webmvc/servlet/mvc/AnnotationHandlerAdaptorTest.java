package com.interface21.webmvc.servlet.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdaptorTest {

    @Test
    void 입력받은_handler가_HandlerExecution의_instance인지_검증한다() {
        // given
        AnnotationHandlerAdaptor handlerAdaptor = new AnnotationHandlerAdaptor();
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        Assertions.assertThat(handlerAdaptor.supports(handler)).isTrue();
    }

    @Test
    void handler를_실행시키고_ModelAndView를_반환한다() {
        // given
        AnnotationHandlerAdaptor handlerAdaptor = new AnnotationHandlerAdaptor();
        HandlerExecution handler = mock(HandlerExecution.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        View view = mock(View.class);
        ModelAndView modelAndView = new ModelAndView(view);
        when(handler.handle(request, response)).thenReturn(modelAndView);

        // when & then
        Assertions.assertThat(handlerAdaptor.handle(request, response, handler))
                .isExactlyInstanceOf(ModelAndView.class);
    }
}
