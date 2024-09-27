package com.techcourse.servlet.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    @DisplayName("controller 클래스를 adapt 한다")
    @Test
    void adaptController() throws Exception {
        HandlerAdapter handlerAdapter = new HandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        doReturn("index.html")
                .when(controller)
                .execute(any(HttpServletRequest.class), any(HttpServletResponse.class));

        assertThatCode(() -> handlerAdapter.adaptHandler(controller, request, response))
                .doesNotThrowAnyException();
    }

    @DisplayName("HandlerExecution 클래스를 adapt 한다")
    @Test
    void adaptHandlerExecution() throws Exception {
        HandlerAdapter handlerAdapter = new HandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ModelAndView stub = mock(ModelAndView.class);

        doReturn(stub)
                .when(handlerExecution)
                .handle(any(HttpServletRequest.class), any(HttpServletResponse.class));

        assertThat(handlerAdapter.adaptHandler(handlerExecution, request, response)).isEqualTo(stub);
    }
}
