package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter;

    @BeforeEach
    void setUp() {
        manualHandlerAdapter = new ManualHandlerAdapter();
    }

    @DisplayName("ManualHandlerAdapter는 Controller 클래스를 지원한다.")
    @Test
    void support_success() {
        // given
        Controller controller = Mockito.mock(Controller.class);

        // when
        boolean result = manualHandlerAdapter.supports(controller);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("ManualHandlerAdapter는 Controller 이외의 클래스는 지원하지 않는다.")
    @Test
    void support_fail() {
        // given
        HandlerExecution handlerExecution = Mockito.mock(HandlerExecution.class);

        // when
        boolean result = manualHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Controller controller = Mockito.mock(Controller.class);

        // when & then
        assertThat(manualHandlerAdapter.handle(request, response, controller))
                .isInstanceOf(ModelAndView.class);
    }
}
