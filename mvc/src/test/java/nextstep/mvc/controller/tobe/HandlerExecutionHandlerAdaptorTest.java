package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HandlerExecutionHandlerAdaptorTest {
    @Test
    @DisplayName("HandlerExecution 객체를 찾아 handle() 메소드를 실행할 수 있다.")
    void getHandlerAndExecute() throws Exception {
        // given
        List<Object> handlers = new ArrayList<>();

        Controller mockController = mock(Controller.class);
        given(mockController.execute(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .willReturn("fail.jsp");

        HandlerExecution mockHandlerExecution = mock(HandlerExecution.class);
        ModelAndView expected = new ModelAndView(new JspView("success"));
        given(mockHandlerExecution.handle(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .willReturn(expected);

        handlers.add(mockController);
        handlers.add(mockHandlerExecution);

        // when
        HandlerExecutionHandlerAdaptor handlerExecutionHandlerAdaptor = new HandlerExecutionHandlerAdaptor();
        Object handler = handlers.stream()
                .filter(handlerExecutionHandlerAdaptor::supports)
                .findFirst()
                .orElseThrow();

        ModelAndView actual = handlerExecutionHandlerAdaptor.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
