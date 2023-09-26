package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.view.JsonView;

class AnnotationHandlerAdaptorTest {

    private static final HandlerAdaptor handlerAdaptor = new AnnotationHandlerAdaptor();

    @Test
    void testSupportsSuccess() {
        //given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        //when
        final boolean result = handlerAdaptor.supports(handlerExecution);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void testSupportsFailWhenHandlerNotHandlerExecution() {
        //given
        final Object obj = mock(Object.class);

        //when
        final boolean result = handlerAdaptor.supports(obj);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void testExecuteSuccess() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        final ModelAndView expected = new ModelAndView(new JsonView());
        when(handlerExecution.handle(request, response))
                .thenReturn(expected);

        //when
        final ModelAndView result = handlerAdaptor.execute(request, response, handlerExecution);

        //then
        assertAll(
                () -> assertThat(result.getViewName()).isEqualTo(null),
                () -> assertThat(result).isEqualTo(expected)
        );
    }
}
