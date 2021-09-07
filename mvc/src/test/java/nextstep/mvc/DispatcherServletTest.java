package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.resolver.ViewResolver;
import nextstep.mvc.view.resolver.ViewResolverImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DispatcherServletTest {

    private final HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);

    private final HandlerMappings handlerMappings = Mockito.mock(HandlerMappings.class);
    private final HandlerAdapters handlerAdapters = Mockito.mock(HandlerAdapters.class);
    private final ViewResolver viewResolver = Mockito.mock(ViewResolverImpl.class);

    @DisplayName("ExceptionHandler로 정의한 방식으로 예외를 처리한다.")
    @Test
    void handleNotFoundException() throws Throwable {
        ExceptionHandlerExecutor exceptionHandlerExecutor = new ExceptionHandlerExecutor("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters, viewResolver, exceptionHandlerExecutor);

        Mockito.when(handlerMappings.getHandler(any())).thenThrow(new UnHandledRequestException());

        ModelAndView modelAndView = dispatcherServlet.processRequest(httpRequest, httpResponse);
        assertThat(modelAndView.getViewName()).isEqualTo("404.html");
    }
}
