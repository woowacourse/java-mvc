package com.techcourse.servlet.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerAdapterTest {

    @DisplayName("controller 클래스라면 adapt를 지원한다")
    @Test
    void supportsHandler() throws Exception {
        ControllerAdapter handlerAdapter = new ControllerAdapter();
        Controller controller = mock(Controller.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertAll(
                () -> assertThat(handlerAdapter.supports(controller)).isTrue(),
                () -> assertThat(handlerAdapter.supports(handlerExecution)).isFalse()
        );
    }

    @DisplayName("controller 클래스를 adapt 한다")
    @Test
    void adaptController() throws Exception {
        ControllerAdapter handlerAdapter = new ControllerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        doReturn("index.html")
                .when(controller)
                .execute(any(HttpServletRequest.class), any(HttpServletResponse.class));

        assertThatCode(() -> handlerAdapter.handle(request, response, controller))
                .doesNotThrowAnyException();
    }
}
