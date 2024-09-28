package com.techcourse.servlet.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution 클래스라면 adapt를 지원한다")
    @Test
    void supportsHandler() throws Exception {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        Controller controller = mock(Controller.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertAll(
                () -> assertThat(handlerAdapter.supports(handlerExecution)).isTrue(),
                () -> assertThat(handlerAdapter.supports(controller)).isFalse()
        );
    }

    @DisplayName("HandlerExecution 클래스를 adapt 한다")
    @Test
    void adaptHandlerExecution() throws Exception {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ModelAndView stub = mock(ModelAndView.class);

        doReturn(stub)
                .when(handlerExecution)
                .handle(any(HttpServletRequest.class), any(HttpServletResponse.class));

        assertThat(handlerAdapter.handle(request, response, handlerExecution)).isEqualTo(stub);
    }
}
