package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
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

class InterfaceBasedControllerHandlerAdapterTest {
    @Test
    @DisplayName("HandlerExecution 객체를 찾아 handle() 메소드를 실행할 수 있다.")
    void getHandlerAndExecute() throws Exception {
        // given
        List<Object> handlers = new ArrayList<>();

        Controller mockController = mock(Controller.class);
        String expectedViewName = "success.jsp";
        given(mockController.execute(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .willReturn(expectedViewName);

        HandlerExecution mockHandlerExecution = mock(HandlerExecution.class);
        ;
        given(mockHandlerExecution.handle(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .willReturn(new ModelAndView(new JspView("fail")));

        handlers.add(mockController);
        handlers.add(mockHandlerExecution);

        // when
        HandlerAdapter handlerExecutionHandlerAdaptor = new InterfaceBasedControllerHandlerAdapter();
        Object handler = handlers.stream()
                .filter(handlerExecutionHandlerAdaptor::supports)
                .findFirst()
                .orElseThrow();

        ModelAndView actual = handlerExecutionHandlerAdaptor.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), handler);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(new ModelAndView(new JspView(expectedViewName)));
    }
}
