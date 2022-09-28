package nextstep.mvc.controller.tobe;

import static nextstep.mvc.controller.tobe.util.TestControllerMethod.FIND_USER_ID;
import static nextstep.mvc.controller.tobe.util.TestControllerMethod.HANDLER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HandlerExecution 은 ")
class HandlerExecutionTest {

    @DisplayName("요청과 응답 객체를 통해 ModelAndView 를 반환한다.")
    @Test
    void handle() throws Exception {

        final HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockedResponse = mock(HttpServletResponse.class);

        when(mockedRequest.getAttribute("id")).thenReturn("gugu");
        when(mockedRequest.getRequestURI()).thenReturn("/get-test");
        when(mockedRequest.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = new HandlerExecution(FIND_USER_ID, HANDLER);
        final ModelAndView modelAndView = handlerExecution.handle(mockedRequest, mockedResponse);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
