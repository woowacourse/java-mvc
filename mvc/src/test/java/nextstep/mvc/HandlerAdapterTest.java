package nextstep.mvc;

import static nextstep.mvc.controller.tobe.util.TestControllerMethod.FIND_USER_ID;
import static nextstep.mvc.controller.tobe.util.TestControllerMethod.HANDLER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HandlerAdapter 는 ")
class HandlerAdapterTest {

    @DisplayName("자신이 지원하는 유형의 객체인지를 판단해야 한다.")
    @Test
    void supportTest() {
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handlerExecution = new HandlerExecution(FIND_USER_ID, HANDLER);
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @DisplayName("ModelAndView 로 핸들러 실행 결과를 가져와야 한다.")
    @Test
    void handle() throws Exception {
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handlerExecution = new HandlerExecution(FIND_USER_ID, HANDLER);
        final HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockedResponse = mock(HttpServletResponse.class);

        when(mockedRequest.getAttribute("id")).thenReturn("gugu");
        when(mockedRequest.getRequestURI()).thenReturn("/get-test");
        when(mockedRequest.getMethod()).thenReturn("GET");

        final ModelAndView modelAndView = handlerAdapter.handle(mockedRequest, mockedResponse, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
